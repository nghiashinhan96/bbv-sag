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


let angularJson = {
  $schema: './node_modules/@angular/cli/lib/config/schema.json',
  version: 1,
  newProjectRoot: 'projects',
  projects: {
    connect8: {
      projectType: 'application',
      schematics: {
        '@schematics/angular:component': {
          style: 'scss'
        }
      },
      root: '',
      sourceRoot: 'src',
      prefix: 'connect',
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
              'src/assets',
              {
                glob: "favicon.ico", // copy only favicon.ico
                input: `./src/assets/favicons/${affiliate}`,
                output: "/" // copy to root directory
              }
            ],
            styles: [
              'node_modules/font-awesome/css/font-awesome.css',
              'node_modules/swiper/swiper.scss',
              'src/styles/main.scss'
            ],
            scripts: [
              'src/assets/js/analytics.js',
              'src/assets/js/gtm.js',
              'src/assets/js/optimizely.js',
              'node_modules/swiper/swiper-bundle.js',
              'node_modules/jsbarcode/dist/JsBarcode.all.min.js'
            ]
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
                  maximumWarning: '3mb',
                  maximumError: '8mb'
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
            browserTarget: 'connect8:build'
          },
          configurations: {
            production: {
              browserTarget: 'connect8:build:production'
            },
            dev: {
              browserTarget: 'connect8:build:dev'
            },
            es5: {
              browserTarget: 'connect8:build:es5'
            }
          }
        },
        'extract-i18n': {
          builder: '@angular-devkit/build-angular:extract-i18n',
          options: {
            browserTarget: 'connect8:build'
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
              "src/assets/favicons/ddch",
              'src/assets'
            ],
            styles: [
              'src/styles/main.scss'
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
            devServerTarget: 'connect8:serve'
          },
          configurations: {
            production: {
              devServerTarget: 'connect8:serve:production'
            }
          }
        }
      }
    }
  },
  defaultProject: 'connect8'
};



if (affiliate && deployment) {
  console.log(`Affiliate:${affiliate}  Deployement: ${deployment}`);
  const environmentUrl = `src/environments/${affiliate}/environment.${deployment}.ts`;
  angularJson.projects.connect8.architect.build.configurations.production.fileReplacements[0].with = environmentUrl;
  angularJson.projects.connect8.architect.build.configurations.dev.fileReplacements[0].with = environmentUrl;
  angularJson.projects.connect8.architect.build.configurations.es5.fileReplacements[0].with = environmentUrl;
  const fs = require('fs');
  fs.writeFileSync('angular.json', JSON.stringify(angularJson, null, 4), 'utf8');
} else {
  throw new Error('Can not load enviroment')
}