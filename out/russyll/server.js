// Compiled by ClojureScript 1.7.145 {:target :nodejs}
goog.provide('russyll.server');
goog.require('cljs.core');
goog.require('cljs.nodejs');
russyll.server.__dirname = __dirname;
russyll.server.express = cljs.nodejs.require.call(null,"express");
russyll.server.app = (new russyll.server.express());
russyll.server.static_route = [cljs.core.str(russyll.server.__dirname),cljs.core.str("/../../resources/public/index.html")].join('');
russyll.server.static_assets = [cljs.core.str(russyll.server.__dirname),cljs.core.str("/../../resources/public/")].join('');
russyll.server.app.use(russyll.server.express.static(russyll.server.static_assets));
russyll.server.app.get("/",(function (req,res){
return res.sendFile(russyll.server.static_route);
}));
russyll.server.port = (5000);
russyll.server._main = (function russyll$server$_main(){
return russyll.server.app.listen(russyll.server.port,(function (){
return console.log([cljs.core.str("App Started at http://localhost:"),cljs.core.str(russyll.server.port)].join(''));
}));
});
cljs.core._STAR_main_cli_fn_STAR_ = russyll.server._main;

//# sourceMappingURL=server.js.map