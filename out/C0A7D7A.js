goog.provide('cljs.nodejs');
goog.require('cljs.core');
cljs.nodejs.require = require;
cljs.nodejs.process = process;
cljs.nodejs.enable_util_print_BANG_ = (function cljs$nodejs$enable_util_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core._STAR_print_fn_STAR_ = (function() { 
var G__12184__delegate = function (args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
};
var G__12184 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__12185__i = 0, G__12185__a = new Array(arguments.length -  0);
while (G__12185__i < G__12185__a.length) {G__12185__a[G__12185__i] = arguments[G__12185__i + 0]; ++G__12185__i;}
  args = new cljs.core.IndexedSeq(G__12185__a,0);
} 
return G__12184__delegate.call(this,args);};
G__12184.cljs$lang$maxFixedArity = 0;
G__12184.cljs$lang$applyTo = (function (arglist__12186){
var args = cljs.core.seq(arglist__12186);
return G__12184__delegate(args);
});
G__12184.cljs$core$IFn$_invoke$arity$variadic = G__12184__delegate;
return G__12184;
})()
;

cljs.core._STAR_print_err_fn_STAR_ = (function() { 
var G__12187__delegate = function (args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
};
var G__12187 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__12188__i = 0, G__12188__a = new Array(arguments.length -  0);
while (G__12188__i < G__12188__a.length) {G__12188__a[G__12188__i] = arguments[G__12188__i + 0]; ++G__12188__i;}
  args = new cljs.core.IndexedSeq(G__12188__a,0);
} 
return G__12187__delegate.call(this,args);};
G__12187.cljs$lang$maxFixedArity = 0;
G__12187.cljs$lang$applyTo = (function (arglist__12189){
var args = cljs.core.seq(arglist__12189);
return G__12187__delegate(args);
});
G__12187.cljs$core$IFn$_invoke$arity$variadic = G__12187__delegate;
return G__12187;
})()
;

return null;
});
