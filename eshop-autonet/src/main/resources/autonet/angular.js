const angularJson = {
    $schema: './node_modules/@angular/cli/lib/config/schema.json',
    version: 1,
    newProjectRoot: 'projects',
    projects: {
        autonet: {
            projectType: 'application',
            schematics: {
                '@schematics/angular:component': {
                    style: 'scss'
                }
            },
            root: '',
            sourceRoot: 'src',
            prefix: 'autonet',
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
                            'src/assets'
                        ],
                        styles: [
                            'node_modules/font-awesome/css/font-awesome.css',
                            'src/styles/main.scss'
                        ],
                        scripts: []
                    },
                    configurations: {
                        production: {
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with: 'src/environments/environment.prod.ts'
                                }
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
                                    maximumError: '5mb'
                                },
                                {
                                    type: 'anyComponentStyle',
                                    maximumWarning: '6kb',
                                    maximumError: '10kb'
                                }
                            ]
                        },
                        dev: {
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with: 'src/environments/environment.ts'
                                }
                            ]
                        },
                        es5: {
                            tsConfig: './tsconfig-es5.app.json',
                            fileReplacements: [
                                {
                                    replace: 'src/environments/environment.ts',
                                    with: 'src/environments/environment.ts'
                                }
                            ]
                        }
                    }
                },
                serve: {
                    builder: '@angular-devkit/build-angular:dev-server',
                    options: {
                        browserTarget: 'autonet:build'
                    },
                    configurations: {
                        production: {
                            browserTarget: 'autonet:build:production'
                        },
                        dev: {
                            browserTarget: 'autonet:build:dev'
                        },
                        es5: {
                            browserTarget: 'autonet:build:es5'
                        }
                    }
                },
                'extract-i18n': {
                    builder: '@angular-devkit/build-angular:extract-i18n',
                    options: {
                        browserTarget: 'autonet:build'
                    }
                },
                test: {
                    builder: '@angular-devkit/build-angular:karma',
                    options: {
                        main: 'src/test.ts',
                        polyfills: 'src/polyfills.ts',
                        tsConfig: 'tsconfig.spec.json',
                        karmaConfig: 'karma.conf.js',
                        assets: [
                            'src/favicon.ico',
                            'src/assets'
                        ],
                        styles: [
                            'src/styles.scss'
                        ],
                        scripts: []
                    }
                },
                lint: {
                    builder: '@angular-devkit/build-angular:tslint',
                    options: {
                        tsConfig: [
                            'tsconfig.app.json',
                            'tsconfig.spec.json',
                            'e2e/tsconfig.json'
                        ],
                        exclude: [
                            '**/node_modules/**'
                        ]
                    }
                },
                e2e: {
                    builder: '@angular-devkit/build-angular:protractor',
                    options: {
                        protractorConfig: 'e2e/protractor.conf.js',
                        devServerTarget: 'autonet:serve'
                    },
                    configurations: {
                        production: {
                            devServerTarget: 'autonet:serve:production'
                        }
                    }
                }
            }
        }
    },
    defaultProject: 'autonet'
};


let deployment = '';
process.argv.forEach((val) => {
    if (val.indexOf('--env') !== -1) {
        deployment = val.replace('--env=', '');
    }
});
if (deployment) {
    const environmentUrl = `src/environments/environment.${deployment}.ts`;
    angularJson.projects.autonet.architect.build.configurations.production.fileReplacements[0].with = environmentUrl;
    angularJson.projects.autonet.architect.build.configurations.dev.fileReplacements[0].with = environmentUrl;
    const fs = require('fs');
    fs.writeFileSync('angular.json', JSON.stringify(angularJson, null, 4), 'utf8');
    console.log(`Export enviroment: ${deployment} done!`);
} else {
    throw new Error('Can not load enviroment')
}