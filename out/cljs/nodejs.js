// Compiled by ClojureScript 1.7.145 {:target :nodejs}
goog.provide('cljs.nodejs');
goog.require('cljs.core');
cljs.nodejs.require = require;
cljs.nodejs.process = process;
cljs.nodejs.enable_util_print_BANG_ = (function cljs$nodejs$enable_util_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core._STAR_print_fn_STAR_ = (function() { 
var G__12192__delegate = function (args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
};
var G__12192 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__12193__i = 0, G__12193__a = new Array(arguments.length -  0);
while (G__12193__i < G__12193__a.length) {G__12193__a[G__12193__i] = arguments[G__12193__i + 0]; ++G__12193__i;}
  args = new cljs.core.IndexedSeq(G__12193__a,0);
} 
return G__12192__delegate.call(this,args);};
G__12192.cljs$lang$maxFixedArity = 0;
G__12192.cljs$lang$applyTo = (function (arglist__12194){
var args = cljs.core.seq(arglist__12194);
return G__12192__delegate(args);
});
G__12192.cljs$core$IFn$_invoke$arity$variadic = G__12192__delegate;
return G__12192;
})()
;

cljs.core._STAR_print_err_fn_STAR_ = (function() { 
var G__12195__delegate = function (args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
};
var G__12195 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__12196__i = 0, G__12196__a = new Array(arguments.length -  0);
while (G__12196__i < G__12196__a.length) {G__12196__a[G__12196__i] = arguments[G__12196__i + 0]; ++G__12196__i;}
  args = new cljs.core.IndexedSeq(G__12196__a,0);
} 
return G__12195__delegate.call(this,args);};
G__12195.cljs$lang$maxFixedArity = 0;
G__12195.cljs$lang$applyTo = (function (arglist__12197){
var args = cljs.core.seq(arglist__12197);
return G__12195__delegate(args);
});
G__12195.cljs$core$IFn$_invoke$arity$variadic = G__12195__delegate;
return G__12195;
})()
;

return null;
});

//# sourceMappingURL=nodejs.js.map