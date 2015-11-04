/**
 * Created by igorf on 25.10.15.
 */
// Bootstrap node.js
require('./out/goog/bootstrap/nodejs');

// Our app compiled by cljsbuild
require('./out/index.js');

// The core of our code
require('./out/russyll/server');

// The core of cljs
require('./out/cljs/core');

// Run main
// NOTE: Dashes in namespaces are replaced by underscores.
russyll.server._main();
