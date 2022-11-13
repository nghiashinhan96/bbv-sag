export class SpinnerService {
    static spinnerContent =
        `<div>
            <div class="loader"></div>
        </div>
        `;
    static text = `<span class="loader-text"></span>`;
    static appSpinnerContent =
        `<div>
            <div class="app-loader"></div>
        </div>
        `;
    // `<i class="fa fa-spinner fa-spin fa-2x fa-fw"></i>
    // <span class="loading-text">Loading...</span>`;
    /**
     * This method support for adding spinner in the input container. Body is the default container
     * @param selector jquery selector for ex: #id, .class, [attribute=attribute_name]...
     * @return jquery element, for deleting the specific spinner
     */
    static start(selector: string = null, options?) {
        this.stop(selector);
        let container = document.body;

        const spinner = document.createElement('div');
        spinner.className = 'spinner overlay-loading';
        spinner.innerHTML = this.spinnerContent;

        if (options && options.class) {
            spinner.classList.add(options.class);
        }
        if (!options || !options.withoutText) {
            const textSpan = document.createElement('span');
            textSpan.classList.add('loader-text');
            textSpan.textContent = 'Loading...';
            spinner.firstChild.appendChild(textSpan);
        }

        if (selector && selector !== 'body') {
            container = document.querySelector(selector) || document.body;
            container.classList.add('spinner-wrapper');
            spinner.classList.add('absolute');
            spinner.setAttribute('name', selector);
            container.style.minHeight = options && options.containerMinHeight || 250;
        }

        container.appendChild(spinner);
        container.focus();
        return spinner;
    }
    /**
     * This method support for removing a runing spinner. by default all spinners will be removed through class 'overlay-loading'
     * @param spinner jquery element or spinner name to remove the sepecific one
     */
    static stop(spinner?: any) {
        if (spinner) {
            if (typeof spinner === 'string') {
                const el = document.querySelector('[name="' + spinner + '"]');
                if (el) {
                    el.parentNode.removeChild(el);
                }
            } else {
                spinner.parentNode && spinner.parentNode.removeChild(spinner);
            }
        } else {
            const overlay = document.querySelector('.spinner.overlay-loading');
            if (overlay) {
                overlay.parentNode.removeChild(overlay);
            }
        }
        const container = document.querySelector('.spinner-wrapper');
        if (container) {
            document.querySelector('.spinner-wrapper').classList.remove('spinner-wrapper');
        }
    }

    static startApp() {
        return this.start(null, {
            spinnerContent: this.appSpinnerContent,
            class: 'app-spinner',
            withoutText: true
        });
    }
}
