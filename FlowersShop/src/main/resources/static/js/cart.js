class CartManager {
    constructor() {
        this.init();
    }

    init() {
        document.addEventListener('DOMContentLoaded', () => {
            this.loadCartCount();
            this.bindEvents();
        });
    }

    async addToCart(productId) {
        try {
            const response = await this.apiRequest('/cart/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `productId=${productId}&quantity=1`
            });

            if (!response.success) {
                this.showNotification(response.message, 'error');
                return;
            }
            if (response.cartCount !== undefined) {
                this.updateCartCounterWithValue(response.cartCount);
            }

            this.showNotification('Product added to cart!', 'success');

        } catch (error) {
            console.error('Error adding to cart:', error);
            this.showNotification('Failed to add product to cart', 'error');
        }
    }

    async updateQuantity(productId, newQuantity) {
        try {
            const response = await this.apiRequest('/cart/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `productId=${productId}&quantity=${newQuantity}`
            });

            if (response.success) {
                this.updateCartDisplay(response);

                if (response.cartCount !== undefined) {
                    this.updateCartCounterWithValue(response.cartCount);
                }
            } else {
                this.showError(response.message || 'Failed to update quantity');
            }
        } catch (error) {
            console.error('Error updating quantity:', error);
            this.showError('Failed to update quantity');
        }
    }

    updateCartDisplay(response) {
        if (window.location.pathname.includes('/cart/')) {
            const quantitySpan = document.querySelector(`.quantity-display[data-product-id="${response.productId}"]`);
            if (quantitySpan && response.quantity !== undefined) {
                quantitySpan.textContent = response.quantity;
            }

            if (response.totalPrice !== undefined) {
                const totalElement = document.getElementById('totalPrice');
                if (totalElement) {
                    totalElement.textContent = response.totalPrice.toFixed(2);
                }
            }
        }
    }



    async removeFromCart(productId) {
        try {
            const response = await this.apiRequest('/cart/remove', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `productId=${productId}`
            });

            if (response.success) {
                if (window.location.pathname.includes('/cart/')) {
                    window.location.reload();
                }

                if (response.cartCount !== undefined) {
                    this.updateCartCounterWithValue(response.cartCount);
                }
            }
        } catch (error) {
            console.error('Error removing from cart:', error);
        }
    }

    async checkout() {
        const checkoutBtn = document.getElementById('checkoutBtn');
        this.setButtonLoading(checkoutBtn, true);

        try {
            const response = await this.apiRequest('/cart/checkout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });

            if (response.success) {
                this.showSuccess('Purchase successful! Thank you for your order!');
                setTimeout(() => window.location.href = '/product/bouquets', 2000);
            } else {
                if (response.redirect) {
                    window.location.href = response.redirect;
                    return;
                }
                this.showError(response.error || 'Checkout failed. Please try again.');
            }
        } catch (error) {
            console.error('Checkout error:', error);
            this.showError('Network error. Please check your connection and try again.');
        } finally {
            this.setButtonLoading(checkoutBtn, false);
        }
    }

    async apiRequest(url, options) {
        const response = await fetch(url, options);
        if (!response.ok && response.status !== 400) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    }
////
    async loadCartCount() {
        try {
            const response = await fetch('/cart/count');
            if (response.ok) {
                const data = await response.json();
                this.updateCartCounterWithValue(data.count || 0);
            }
        } catch (error) {
            console.error('Error loading cart count:', error);
        }
    }

    updateCartCounterWithValue(count) {
        const counter = document.getElementById('cartCounter');
        if (!counter) return;

        counter.textContent =  counter.textContent + count;
        counter.style.display = count > 0 ? 'inline' : 'none';
    }
    ////


    showNotification(message, type = 'success') {
        const notification = document.getElementById('cartNotification');
        if (!notification) return;

        notification.textContent = message;
        notification.className = `cart-notification ${type}`;
        notification.style.display = 'block';

        setTimeout(() => notification.style.display = 'none', 3000);
    }

    showSuccess(message) {
        this.showMessage('successMessage', message);
        this.hideMessage('errorMessage');
    }

    showError(message) {
        this.showMessage('errorMessage', message);
        this.hideMessage('successMessage');
    }

    showMessage(elementId, message) {
        const element = document.getElementById(elementId);
        if (element) {
            element.textContent = message;
            element.style.display = 'block';
        }
    }

    hideMessage(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.style.display = 'none';
        }
    }

    setButtonLoading(button, loading) {
        if (!button) return;

        if (loading) {
            button.disabled = true;
            button.textContent = 'Processing...';
        } else {
            button.disabled = false;
            button.textContent = 'Checkout';
        }
    }

    bindEvents() {
        document.querySelectorAll('.add-to-cart-btn').forEach(button => {
            button.addEventListener('click', () => {
                const productId = parseInt(button.getAttribute('data-product-id'));
                const productName = button.getAttribute('data-product-name');
                const productPrice = parseFloat(button.getAttribute('data-product-price'));
                this.addToCart(productId, productName, productPrice);
            });
        });
    }
}




const cartManager = new CartManager();

function goToCart() {
    window.location.href = '/cart/';
}

function goBack() {
    window.location.href = '/product/bouquets';
}

function checkout() {
    cartManager.checkout();
}

function updateQuantity(productId, change) {
    const quantitySpan = document.querySelector(`.quantity-display[data-product-id="${productId}"]`);
    if (quantitySpan) {
        const currentQuantity = parseInt(quantitySpan.textContent) || 1;
        const newQuantity = Math.max(1, currentQuantity + change);
        if (newQuantity !== currentQuantity) {
            cartManager.updateQuantity(productId, newQuantity);
        }
    }
}
function removeFromCart(productId) {
    cartManager.removeFromCart(productId);
}

function addToCart(productId, productName, productPrice) {
    cartManager.addToCart(productId, productName, productPrice);
}