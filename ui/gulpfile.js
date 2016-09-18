const gulp = require('gulp');
const del = require('del');
const typescript = require('gulp-typescript');
const tscConfig = require('./tsconfig.json');
const sourcemaps = require('gulp-sourcemaps');
const browserSync = require('browser-sync').create();
const reload = browserSync.reload;
const proxy = require('http-proxy-middleware');
const historyApiFallback = require('connect-history-api-fallback');

// clean the contents of the distribution directory
gulp.task('clean', function () {
  return del('dist/**/*');
});

// TypeScript compile
gulp.task('compile', ['clean'], function () {
  return gulp
    .src('app/**/*.ts')
    .pipe(sourcemaps.init())
    .pipe(typescript(tscConfig.compilerOptions))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('dist/app'));
});

// copy dependencies
gulp.task('copy:libs', ['clean'], function() {
  return gulp.src([
      'node_modules/core-js/client/shim.min.js',
      'node_modules/zone.js/dist/zone.js',
      'node_modules/reflect-metadata/Reflect.js',
      'node_modules/systemjs/dist/system.src.js',
      'node_modules/@angular/**/*.umd.js',
      'node_modules/rxjs/**/*.js'
    ], { base : 'node_modules' })
    .pipe(gulp.dest('dist/lib'))
});

// copy static assets - i.e. non TypeScript compiled source
gulp.task('copy:assets', ['clean'], function() {
  return gulp.src(['app/**/*', 'index.html', 'styles.css', '!app/**/*.ts', 'systemjs.config.js', 'img/**/*'], { base : './' })
    .pipe(gulp.dest('dist'))
});

gulp.task('serve', ['build'], function() {
  browserSync.init({
    server: {
      baseDir: 'dist',
      middleware: [
        proxy('/api', {target: 'http://localhost:8080'}),
        historyApiFallback()
      ]
    }
  });

  gulp.watch(['app/**/*', 'index.html', 'styles.css'], ['buildAndReload']);
});

gulp.task('build', ['compile', 'copy:libs', 'copy:assets']);
gulp.task('buildAndReload', ['build'], reload);
gulp.task('default', ['build']);
