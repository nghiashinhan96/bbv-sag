ConnectOptimizely = {
    initiated: false,
    currencyCode: null,

    setCurrencyCode: function (currencyCode) {
		if (currencyCode) {
			this.currencyCode = currencyCode;
		}
	},

    addRevenue(data) {
        if (!this.initiated) {
            return;
        }
        const eventName = 'revenue_' + (this.currencyCode || '').toLowerCase();
        window['optimizely'] = window['optimizely'] || [];
        window['optimizely'].push({
            type: "event",
            eventName,
            tags: {
                revenue: Math.round(data.subTotalWithNet * 100)
            }
        });
    }
}