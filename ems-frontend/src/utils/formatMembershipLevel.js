export const formatMembershipLevel = (levelName, levelCode) => {
  const code = levelCode || "BRONZE";
  const name = levelName?.trim();
  if (name && name !== code) {
    return `${name} (${code})`;
  }
  return name || code;
};
