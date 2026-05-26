import { useCallback, useEffect, useRef, useState } from "react";
import { toast } from "react-toastify";
import {
  getMyProfile,
  listProductsForMe,
  purchaseProductForMe,
} from "../services/StudentPortalService";

const PURCHASE_INTERVAL_MS = 1000;

const useStudentProductPurchaseHook = () => {
  const [products, setProducts] = useState([]);
  const [deposit, setDeposit] = useState(0);
  const [membershipPoints, setMembershipPoints] = useState(0);
  const [quantities, setQuantities] = useState({});
  const lastPurchaseTimestamps = useRef(new Map());

  const fetchProfile = useCallback(async () => {
    try {
      const response = await getMyProfile();
      setDeposit(Number(response.data.deposit ?? 0));
      setMembershipPoints(Number(response.data.membershipPoints ?? 0));
    } catch (err) {
      console.log(err);
    }
  }, []);

  const fetchProducts = useCallback(async () => {
    try {
      const response = await listProductsForMe();
      setProducts(response.data);
      setQuantities((prev) => {
        const next = { ...prev };
        response.data.forEach((product) => {
          if (next[product.id] === undefined) {
            next[product.id] = 0;
          }
        });
        return next;
      });
    } catch (err) {
      console.log(err);
    }
  }, []);

  useEffect(() => {
    fetchProfile();
    fetchProducts();
  }, [fetchProfile, fetchProducts]);

  const setQuantity = (productId, value) => {
    const parsed = Number(value);
    const quantity = Number.isNaN(parsed) || parsed < 0 ? 0 : Math.floor(parsed);
    setQuantities((prev) => ({ ...prev, [productId]: quantity }));
  };

  const handlePurchase = async (product) => {
    const quantity = quantities[product.id] ?? 0;
    const price = Number(product.price ?? 0);
    if (quantity <= 0) {
      toast.error("购买数量必须大于 0");
      return;
    }
    if (price <= 0) {
      toast.error("该商品未定价，请联系管理员");
      return;
    }
    if (product.stock <= 0) {
      toast.error("该商品暂无库存");
      return;
    }

    const now = Date.now();
    const lastPurchase = lastPurchaseTimestamps.current.get(product.id) ?? 0;
    if (now - lastPurchase < PURCHASE_INTERVAL_MS) {
      toast.info("请 1 秒后再购买该商品");
      return;
    }
    lastPurchaseTimestamps.current.set(product.id, now);

    try {
      const response = await purchaseProductForMe(product.id, quantity);
      const totalCost = Number(response.data.totalCost ?? 0);
      setDeposit(Number(response.data.remainingDeposit ?? 0));
      toast.success(`购买成功，已扣款 ${totalCost.toFixed(2)} 元`);
      setQuantities((prev) => ({ ...prev, [product.id]: 0 }));
      fetchProducts();
      fetchProfile();
    } catch (err) {
      toast.error(err.response?.data?.message || "购买失败");
    }
  };

  const formatPrice = (price) => {
    const value = Number(price ?? 0);
    return Number.isNaN(value) ? "0.00" : value.toFixed(2);
  };

  const formatDeposit = (value) => {
    const amount = Number(value ?? 0);
    return Number.isNaN(amount) ? "0.00" : amount.toFixed(2);
  };

  const isProductPriced = (product) => Number(product.price ?? 0) > 0;

  return {
    products,
    deposit,
    membershipPoints,
    quantities,
    setQuantity,
    handlePurchase,
    formatPrice,
    formatDeposit,
    isProductPriced,
  };
};

export default useStudentProductPurchaseHook;
