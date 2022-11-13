export class SpinnerService {
    static spinnerContent =
        `<div>
            <i class="fa fa-spinner fa-spin fa-2x fa-fw"></i>
        </div>
        `;
    static appSpinnerContent =
        `<div>
            <div class="app-loader"></div>
        </div>
        `;
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
        spinner.innerHTML = options && options.spinnerContent || this.spinnerContent;

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
            if (options && (options.containerMinHeight || options.containerMinHeight === 0)) {
                container.style.minHeight = options.containerMinHeight + 'px';
            } else {
                container.style.minHeight = '200px';
            }
            container.style.position = 'relative';
        }

        if (options && options.class) {
            spinner.className = `${spinner.className} ${options.class}`;
        }

        container.appendChild(spinner);
        container.click();
        return spinner;
    }
    /**
     * This method support for removing a runing spinner. by default all spinners will be removed through class 'overlay-loading'
     * @param spinner jquery element or spinner name to remove the sepecific one
     */
    static stop(spinner?: any) {
        let container;
        if (spinner) {
            if (typeof spinner === 'string') {
                const el = document.querySelector('[name="' + spinner + '"]');
                if (el) {
                    container = el.parentNode;
                    container.removeChild(el);
                }
            } else {
                if (spinner.parentNode) {
                    container = spinner.parentNode;
                    container.removeChild(spinner);
                }
            }
        } else {
            const overlay = document.querySelector('.spinner.overlay-loading');
            if (overlay) {
                container = overlay.parentNode;
                container.removeChild(overlay);
            }
        }

        if (container) {
            container.classList.remove('spinner-wrapper');
            container.style.minHeight = null;
            container.style.position = null;
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
