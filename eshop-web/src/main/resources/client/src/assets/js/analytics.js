GoogleAnalytics = {
	trackingCode: null,
	userId: null,
	currencyCode: null,
	initiated: false,

	enabled: function () {
		return this.trackingCode != null;
	},
	setTracingCode: function (code) {
		console.log("Verifying set tracing code");
		this.trackingCode = code;
	},
	setUserId: function (id) {
		console.log("Verifying set User ID", id);
		if (id && id != '/') {
			this.userId = id;
		}
	},
	setVehicleInfo: function (vehicleBrand, vehicleCode) {
		if (vehicleBrand) {
			ga('set', 'dimension2', vehicleBrand);
		}
		if (vehicleCode) {
			ga('set', 'dimension3', vehicleCode);
		}
	},
	setCurrencyCode: function (currencyCode) {
		if (currencyCode) {
			this.currencyCode = currencyCode;
		}
	},
	initiate: function () {
		if (this.enabled() && !this.initiated) {
			(function (i, s, o, g, r, a, m) {
				i['GoogleAnalyticsObject'] = r;
				i[r] = i[r] || function () {
					(i[r].q = i[r].q || []).push(arguments)
				}, i[r].l = 1 * new Date();
				a = s.createElement(o), m = s.getElementsByTagName(o)[0];
				a.async = 1;
				a.src = g;
				m.parentNode.insertBefore(a, m)
			})(window, document, 'script',
				'//www.google-analytics.com/analytics.js', 'ga');

			ga('create', {
				trackingId: this.trackingCode,
				cookieDomain: 'auto'
			});
			if (this.userId) {
				ga('set', 'userId', this.userId);
				ga('set', 'dimension1', this.userId);
			}
			if (this.currencyCode) {
				ga('set', 'currencyCode', this.currencyCode);
			}
			this.initiated = true;
			console.log("Initialized the GA connection");
		}
	},
	clear: function() {
		this.initiated = false;
	},
	sendPageView: function (newpage) {
		if (this.enabled()) {
			console.log("Send page view " + newpage + " to Google Analytics");
			this.initiate();
			ga('set', 'page', newpage);
			ga('send', 'pageview');
		}
	},
	sendQueryParameters: function (page, params) {
		if (this.enabled()) {
			this.initiate();

			var page = page || '/search';
			if (params) {
				console.log("Send page view(sendQueryParameters) to Google Analytics", page + '?' + params);
				ga('send', 'pageview', page + '?' + params);
			}
		}
	},
	ec: {
		loaded: false,
		load: function () {
			if (GoogleAnalytics.enabled() && !this.loaded) {
				GoogleAnalytics.initiate();
				ga('require', 'ec');
				this.loaded = true;
			}
		},
		addImpressions: function (impressions, list) {
			if (GoogleAnalytics.enabled() && impressions) {
				this.load();
				for (var i = 0; i < impressions.length; i++) {
					var impressionFieldObject = impressions[i];
					if (impressionFieldObject.list == null) {
						impressionFieldObject.list = list || 'Search Results';
					}
					ga('ec:addImpression', impressionFieldObject)
				}

			}
		},
		viewProductDetails: function (productFieldObject) {
			if (GoogleAnalytics.enabled() && productFieldObject) {
				this.load();
				ga('ec:addProduct', productFieldObject);
				ga('ec:setAction', 'detail', {
					list: 'Search Results'
				});
				ga('send', 'event', 'Article', 'detail', 'Show article details');
			}
		},
		updateBasketProduct: function (args) {
			if (GoogleAnalytics.enabled() && args && args.product) {
				this.load();
				var prod = args.product;
				if (typeof args.product != 'object') {
					prod = JSON.parse(prod);
				}
				if (args.newToBasket) {
					this.addProductToBasket(prod)
				} else {
					var oldQuantity = prod.quantity;
					var newQuantity = args.quantity;

					if (newQuantity > oldQuantity) {
						prod.quantity = newQuantity - oldQuantity;
						this.addProductToBasket(prod);
					} else if (newQuantity < oldQuantity) {
						prod.quantity = oldQuantity - newQuantity;
						this.removeProductFromBasket(prod);
					}
				}
			}
		},
		addBasketProducts: function (args) {
			if (GoogleAnalytics.enabled() && args && args.products) {
				this.load();
				var prods = args.products;
				if (typeof args.products != 'object') {
					prods = JSON.parse(prods);
				}
				if (prods.length > 0) {
					for (i = 0; i < prods.length; i++) {
						ga('ec:addProduct', prods[i]);
					}
					ga('ec:setAction', 'add');
					ga('send', 'event', 'Article', 'Add',
						'Add article to basket');
				}

			}
		},
		addProductToBasket: function (productFieldObject) {
			if (GoogleAnalytics.enabled() && productFieldObject) {
				var prod = productFieldObject;
				if (typeof productFieldObject != 'object') {
					prod = JSON.parse(prod);
				}
				this.load();
				console.log('Added product to basket', productFieldObject);
				ga('ec:addProduct', prod);
				ga('ec:setAction', 'add');
				ga('send', 'event', 'Article', 'Add', 'Add article to basket');
			}
		},
		removeProductFromBasket: function (productFieldObject) {
			if (GoogleAnalytics.enabled() && productFieldObject) {
				var prod = productFieldObject;
				if (typeof productFieldObject != 'object') {
					prod = JSON.parse(prod);
				}
				this.load();
				console.log('Removed product from basket', productFieldObject);
				ga('ec:addProduct', prod);
				ga('ec:setAction', 'remove');
				ga('send', 'event', 'Article', 'Remove',
					'Remove article from basket');
			}
		},
		checkout: function (products) {
			if (GoogleAnalytics.enabled() && products) {
				this.load();
				var prods = products;
				if (typeof prods != 'object') {
					prods = JSON.parse(prods);
				}
				if (prods.length > 0) {
					for (i = 0; i < prods.length; i++) {
						ga('ec:addProduct', prods[i]);
					}
					ga('ec:setAction', 'checkout');
				}
			}
		},
		purchase: function (args) {
			if (GoogleAnalytics.enabled() && args && args.products
				&& args.actionField) {
				this.load();
				var prods = args.products;
				if (typeof prods != 'object') {
					prods = JSON.parse(prods);
				}
				var af = args.actionField;
				if (typeof af != 'object') {
					af = JSON.parse(af);
				}
				if (prods.length > 0) {
					console.log('Purchase the shopping basket => product data', prods);
					for (i = 0; i < prods.length; i++) {
						ga('ec:addProduct', prods[i]);
					}
					console.log('Purchase the shopping basket => action field', af);
					ga('ec:setAction', 'purchase', af);
					ga('send', 'pageview');
				}
			}
		}
	}
};
