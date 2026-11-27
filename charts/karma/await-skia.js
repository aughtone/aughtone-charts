// Holds the browser tests until Skia's wasm can be called. On Kotlin/JS, skiko publishes its
// functions on window and loads its wasm asynchronously, so a Compose UI test that starts first
// fails with "org_jetbrains_skia_... is not a function". Kotlin/Wasm links Skia directly and
// never exposes these, so after a few seconds the hook lets the tests run regardless.
before(function () {
    this.timeout(15000);
    var deadline = Date.now() + 10000;
    return new Promise(function (resolve) {
        (function poll() {
            try {
                var pointer = window.malloc(8);
                window.free(pointer);
                return resolve();
            } catch (notReady) {
                if (Date.now() > deadline) return resolve();
                setTimeout(poll, 20);
            }
        })();
    });
});
