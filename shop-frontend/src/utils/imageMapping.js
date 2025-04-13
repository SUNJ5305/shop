const imageMapping = {
    1: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e',
    2: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff',
    3: 'https://images.unsplash.com/photo-1551963831-b3b1ca40c98e',
    default: 'https://via.placeholder.com/150?text=Default+Image',
};

export const getProductImage = (productId) => {
    return imageMapping[productId] || imageMapping.default;
};