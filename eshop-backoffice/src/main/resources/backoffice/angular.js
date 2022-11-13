let angularJson = {
    $schema: './node_modules/@angular/cli/lib/config/schema.json',
    version: 1,
    newProjectRoot: 'projects',
    projects: {
        backoffice8: {
            projectType: 'application',
            schematics: {
                '@schematics/angular:component': {
                    style: 'scss',
                },
            },
            root: '',
            sourceRoot: 'src',
            prefix: 'backoffice',
            architect: {
                build: {
                    builder: '@angular-devkit/build-angular:browser',
                    options: {
                        outputPath: '../public',
                        index: 'src/index.html',
                        main: 'src/main.ts',
                        polyfills: 'src/polyfills.ts',
                        tsConfig: 'tsconfig.app.json',
                        aot: false,
                        assets: [
                            'src/favicon.ico',
                            'src/assets',
                            {
                                glob: '**/*',
                                input: 'node_modules/ace-builds/src/',
                                output: '/',
                            },
                        ],
                        styles: [
                            'node_modules/font-awesome/css/font-awesome.css',
                            'src/styles/main.scss',
                            'node_modules/ngx-markdown-editor/assets/highlight.js/agate.min.css',
                        ],
                        scripts: [
                            'node_modules/ngx-markdown-editor/assets/highlight.js/highlight.min.js',
                            'node_modules/ngx-markdown-editor/assets/marked.min.js',
                            'node_modules/ace-builds/src-min/ace.js',
                            'node_modules/ace-builds/src/theme-eclipse.js',
                            'node_modules/ace-builds/src/theme-monokai.js',
                            'node_modules/ace-builds/src/mode-html.js',
                        ],
                    },
                    configurations: {
                        production: {
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with:
                                        'src/environments/environment.prod.ts',
                                },
                            ],
                            optimization: true,
                            outputHashing: 'all',
                            sourceMap: false,
                            extractCss: true,
                            namedChunks: false,
                            aot: true,
                            extractLicenses: true,
                            vendorChunk: false,
                            buildOptimizer: true,
                            budgets: [
                                {
                                    type: 'initial',
                                    maximumWarning: '2mb',
                                    maximumError: '5mb',
                                },
                                {
                                    type: 'anyComponentStyle',
                                    maximumWarning: '6kb',
                                    maximumError: '10kb',
                                },
                            ],
                        },
                        dev: {
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with: 'src/environments/environment.ts',
                                },
                            ],
                        },
                        es5: {
                            tsConfig: './tsconfig-es5.app.json',
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with: 'src/environments/environment.ts',
                                },
                            ],
                        },
                    },
                },
                serve: {
                    builder: '@angular-devkit/build-angular:dev-server',
                    options: {
                        browserTarget: 'backoffice8:build',
                    },
                    configurations: {
                        production: {
                            browserTarget: 'backoffice8:build:production',
                        },
                        dev: {
                            browserTarget: 'backoffice8:build:dev',
                        },
                        es5: {
                            browserTarget: 'backoffice8:build:es5',
                        },
                    },
                },
                'extract-i18n': {
                    builder: '@angular-devkit/build-angular:extract-i18n',
                    options: {
                        browserTarget: 'backoffice8:build',
                    },
                },
                test: {
                    builder: '@angular-devkit/build-angular:karma',
                    options: {
                        main: 'src/test.ts',
                        polyfills: 'src/polyfills.ts',
                        tsConfig: 'tsconfig.spec.json',
                        karmaConfig: 'karma.conf.js',
                        assets: ['src/favicon.ico', 'src/assets'],
                        styles: ['src/styles.scss'],
                        scripts: [],
                    },
                },
                lint: {
                    builder: '@angular-devkit/build-angular:tslint',
                    options: {
                        tsConfig: [
                            'tsconfig.app.json',
                            'tsconfig.spec.json',
                            'e2e/tsconfig.json',
                        ],
                        exclude: ['**/node_modules/**'],
                    },
                },
                e2e: {
                    builder: '@angular-devkit/build-angular:protractor',
                    options: {
                        protractorConfig: 'e2e/protractor.conf.js',
                        devServerTarget: 'backoffice8:serve',
                    },
                    configurations: {
                        production: {
                            devServerTarget: 'backoffice8:serve:production',
                        },
                    },
                },
            },
        },
    },
    defaultProject: 'backoffice8',
};

let affiliate = '';
let deployment = '';
process.argv.forEach((val) => {
    if (val.indexOf('--env') !== -1) {
        const evnVal = val.replace('--env=', '');
        const envValArr = evnVal.split('-');
        affiliate = envValArr[0];
        deployment = envValArr[1];
    }
});
if (affiliate && deployment) {
    console.log(`Affiliate:${affiliate}  Deployement: ${deployment}`);
    const environmentUrl = `src/environments/${affiliate}/environment.${deployment}.ts`;
    angularJson.projects.backoffice8.architect.build.configurations.production.fileReplacements[0].with = environmentUrl;
    angularJson.projects.backoffice8.architect.build.configurations.dev.fileReplacements[0].with = environmentUrl;
    angularJson.projects.backoffice8.architect.build.configurations.es5.fileReplacements[0].with = environmentUrl;
    const fs = require('fs');
    fs.writeFileSync(
        'angular.json',
        JSON.stringify(angularJson, null, 4),
        'utf8'
    );
} else {
    throw new Error('Can not load enviroment');
}
