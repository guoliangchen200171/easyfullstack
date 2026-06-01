import { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import {
  addProductStock,
  addProductStockByName,
  createProduct,
  deductProductStock,
  deductProductStockByName,
  deleteProduct,
  listProducts,
  updateProduct,
} from "../services/ProductService";

const useProductManagementHook = () => {
  const [products, setProducts] = useState([]);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [stock, setStock] = useState(0);
  const [stockMode, setStockMode] = useState("id");       // "id" | "name"
  const [stockIdentifier, setStockIdentifier] = useState("");
  const [stockQuantity, setStockQuantity] = useState(1);
  const [editingId, setEditingId] = useState(null);
  const [editDraft, setEditDraft] = useState({
    name: "",
    description: "",
    price: 0,
  });

  const fetchProducts = useCallback(async () => {
    try {
      const response = await listProducts();
      setProducts(response.data);
    } catch (err) {
      console.log(err);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const handleStockModeChange = (mode) => {
    setStockMode(mode);
    setStockIdentifier("");
  };

  const parseStockOperationInputs = () => {
    const quantity = Number(stockQuantity);
    if (Number.isNaN(quantity) || quantity <= 0) {
      toast.error("数量必须大于 0");
      return null;
    }
    if (stockMode === "id") {
      const productId = Number(stockIdentifier);
      if (!stockIdentifier.trim() || Number.isNaN(productId) || productId <= 0) {
        toast.error("请输入有效的商品 ID");
        return null;
      }
      return { mode: "id", productId, quantity };
    } else {
      if (!stockIdentifier.trim()) {
        toast.error("请输入商品名称");
        return null;
      }
      return { mode: "name", name: stockIdentifier.trim(), quantity };
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      toast.error("商品名称不能为空");
      return;
    }
    const priceValue = Number(price);
    if (Number.isNaN(priceValue) || priceValue <= 0) {
      toast.error("价格必须大于 0");
      return;
    }
    if (stock < 0) {
      toast.error("库存不能为负数");
      return;
    }
    try {
      await createProduct({
        name: name.trim(),
        description: description.trim(),
        price: priceValue,
        stock: Number(stock),
      });
      toast.success("商品新增成功");
      setName("");
      setDescription("");
      setPrice(0);
      setStock(0);
      fetchProducts();
    } catch (err) {
      toast.error(err.response?.data?.message || "新增失败");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("确定删除该商品吗？")) {
      return;
    }
    try {
      await deleteProduct(id);
      toast.success("商品已删除");
      if (editingId === id) {
        setEditingId(null);
      }
      fetchProducts();
    } catch (err) {
      toast.error(err.response?.data?.message || "删除失败");
    }
  };

  const handleAddStock = async () => {
    const inputs = parseStockOperationInputs();
    if (!inputs) return;
    try {
      if (inputs.mode === "id") {
        await addProductStock(inputs.productId, inputs.quantity);
      } else {
        await addProductStockByName(inputs.name, inputs.quantity);
      }
      toast.success("库存已增加");
      fetchProducts();
    } catch (err) {
      toast.error(err.response?.data?.message || "加库存失败");
    }
  };

  const handleDeductStock = async () => {
    const inputs = parseStockOperationInputs();
    if (!inputs) return;
    try {
      if (inputs.mode === "id") {
        await deductProductStock(inputs.productId, inputs.quantity);
      } else {
        await deductProductStockByName(inputs.name, inputs.quantity);
      }
      toast.success("库存已扣减");
      fetchProducts();
    } catch (err) {
      toast.error(err.response?.data?.message || "扣库存失败");
    }
  };

  const startEdit = (product) => {
    setEditingId(product.id);
    setEditDraft({
      name: product.name,
      description: product.description || "",
      price: product.price ?? 0,
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditDraft({ name: "", description: "", price: 0 });
  };

  const handleSaveEdit = async (product) => {
    if (!editDraft.name.trim()) {
      toast.error("商品名称不能为空");
      return;
    }
    const priceValue = Number(editDraft.price);
    if (Number.isNaN(priceValue) || priceValue <= 0) {
      toast.error("价格必须大于 0");
      return;
    }
    try {
      await updateProduct(product.id, {
        name: editDraft.name.trim(),
        description: editDraft.description.trim(),
        price: priceValue,
        stock: product.stock,
      });
      toast.success("商品信息已更新");
      setEditingId(null);
      fetchProducts();
    } catch (err) {
      toast.error(err.response?.data?.message || "更新失败");
    }
  };

  return {
    products,
    name,
    setName,
    description,
    setDescription,
    price,
    setPrice,
    stock,
    setStock,
    stockMode,
    handleStockModeChange,
    stockIdentifier,
    setStockIdentifier,
    stockQuantity,
    setStockQuantity,
    editingId,
    editDraft,
    setEditDraft,
    handleCreate,
    handleDelete,
    handleAddStock,
    handleDeductStock,
    startEdit,
    cancelEdit,
    handleSaveEdit,
  };
};

export default useProductManagementHook;
