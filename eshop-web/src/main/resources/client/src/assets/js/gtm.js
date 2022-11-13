ConnectGoogleTagManager = {
	userType: null,
	customerNr: null,
	userId: null,
	gtmContainerId: null,
	gtmAuth: null,
	gtmEnv: null,
	currencyCode: null,
	initiated: false,

	enabled: function () {
		return this.gtmContainerId && this.gtmAuth && this.gtmEnv;
	},
	setData: function (data) {
        if (!data) {
            return;
        }
        this.gtmContainerId = data.gtm_container_id;
		this.gtmAuth = data.gtm_auth;
        this.gtmEnv = data.gtm_env;

	},
	setUserData: function(data) {
		if (!data) {
            return;
        }
		this.userType = data.userType;
		this.customerNr = data.customerNr;
		this.userId = (data.userId || '').toString();
	},
	setCurrencyCode: function (currencyCode) {
		if (currencyCode) {
			this.currencyCode = currencyCode;
		}
	},
	initiate: function () {
		if (this.enabled() && !this.initiated) {
            var gtmContainerId = this.gtmContainerId;
            var gtmAuth = this.gtmAuth;
            var gtmEnv = this.gtmEnv;
			
			(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
			new Date().getTime(),event:'gtm.js'});var f=d.head.firstChild,
			j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
			'https://www.googletagmanager.com/gtm.js?id='+i+dl+'&gtm_auth=' + gtmAuth + '&gtm_preview='+gtmEnv+'&gtm_cookies_win=x';f.parentNode.insertBefore(j,f);
			})(window,document,'script','dataLayer',gtmContainerId);

            (function(w,d,i){
                var n=d.createElement('noscript'), f=d.createElement('iframe');
                f.height=0;
                f.width=0;
                f.style='display:none;visibility:hidden';
                f.src='https://www.googletagmanager.com/ns.html?id='+i+'&gtm_auth='+gtmAuth+'&gtm_preview='+gtmEnv+'&gtm_cookies_win=x';
                n.appendChild(f);
                var s = d.body.firstChild;
                s.parentNode.insertBefore(n, s);
            })(window,document,gtmContainerId);
			this.initiated = true;
			console.log("Initialized the GTM");
		}
	},
	clear: function() {
		this.initiated = false;
	},
	ec: {
		loaded: false,
		load: function () {
			if (ConnectGoogleTagManager.enabled() && !this.loaded) {
				ConnectGoogleTagManager.initiate();
				this.loaded = true;
			}
		},
		addUserData: function() {
			var userData = {
				user_type: ConnectGoogleTagManager.userType,
				user_id: ConnectGoogleTagManager.customerNr,
				crm_id: ConnectGoogleTagManager.customerNr,
				client_id: ConnectGoogleTagManager.userId
			};
			dataLayer.push(userData);
		},
		clearEcommerceData: function() {
			var data = {
				ecommerce: null
			};
			dataLayer.push(data);
		},
		login: function(args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				dataLayer.push({
					event: "login",
					method: "email",
					user_type: args.userType,
					user_id: ConnectGoogleTagManager.customerNr,
					crm_id: ConnectGoogleTagManager.customerNr,
					client_id: ConnectGoogleTagManager.userId
				});
			}
		},
		search: function(args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "search",
					search_term: args.searchTerm,
					source_id: args.source_id,
					source_description: args.source_desc
				});
			}
		},
		viewProductList: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "view_item_list",
					ecommerce: {
						items: args
					}
				});
			}
		},
		viewProductDetails: function (args, value) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "view_item",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: value,
						items: [args]
					}
				});
			}
		},
		selectProduct: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "select_item",
					ecommerce: {
						items: [args]
					}
				});
			}
		},
		addToWishlist: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "add_to_wishlist",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: args.summary && args.summary.total,
						items: [args.article]
					}
				});
			}
		},
		updateBasketProduct: function (args, value) {
			if (ConnectGoogleTagManager.enabled() && args && args.product) {
				this.load();
				var prod = args.product;

				prod.quantity = args.quantity;
				this.addProductToBasket(prod, value);
			}
		},
		addProductToBasket: function (args, value) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "add_to_cart",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: value,
						items: [args]
					}
				});
			}
		},
		removeProductFromBasket: function (args, value) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "remove_from_cart",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: value,
						items: [args]
					}
				});
			}
		},
		viewShoppingCart: function(args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "view_cart",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: args.summary && args.summary.subTotalExlVat,
						items: args.articles
					}
				});
			}
		},
		beginCheckout: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "begin_checkout",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: args.summary && args.summary.subTotalExlVat,
						items: args.articles
					}
				});
			}
		},
		purchase: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "purchase",
					ecommerce: {
						transaction_id: args.summary.transaction_id,
						value: args.summary && args.summary.value,
						tax: args.summary && args.summary.tax,
						currency: ConnectGoogleTagManager.currencyCode,
						items: args.articles
					}
				});
			}
		},
		addShippingInfo: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "add_shipping_info",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: args.value,
						shipping_tier: args.shipping_tier,
						items: args.articles
					}
				});
			}
		},
		addPaymentInfo: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				this.clearEcommerceData();
				dataLayer.push({
					event: "add_payment_info",
					ecommerce: {
						currency: ConnectGoogleTagManager.currencyCode,
						value: args.value,
						payment_type: args.payment_type,
						items: args.articles
					}
				});
			}
		},
		signUp: function (args) {
			if (ConnectGoogleTagManager.enabled() && args) {
				this.load();
				dataLayer.push({
					event: "sign_up",
					method: args.method,
					step: args.step
				});
			}
		}
	}
};
