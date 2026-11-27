// Compose's UI tests draw through Skia, whose wasm loads asynchronously on Kotlin/JS, after the
// test bundle has started. This adds a mocha hook that holds the tests until Skia is ready.
// basePath is <root>/build/js/packages/<package>, so the hook lives four levels up.
config.files.push(require("path").resolve(config.basePath, "../../../../charts/karma/await-skia.js"));
