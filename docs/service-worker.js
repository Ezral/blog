"use strict";var precacheConfig=[["/404.html","f0e611127d3aaaa6856314b6f6895750"],["/Almace-Scaffolding-quick-post.html","13e72f6d8e4f12e6fdc1a2d377db19d8"],["/Dataquest-guided-project-3.html","d5e40849c3544811201ab4c434fd238c"],["/Displaying-math-formula-on-ASMF.html","fa64c5b1f294a1169ed3abbbef60ca28"],["/Guided-project-Mobile-App-for-lottery-addiction.html","c89e8de480cded76be01a380fd1c6127"],["/Proportion-of-Doctors-in-Indonesia.html","939eccfdb6969861b6f2e61f834be418"],["/Reviewing-ECG.html","c9fc4e03e0ce366e9e0f74c1d405565b"],["/about/index.html","cd0bae0e62e8bbdc78919b13db151beb"],["/air-medical-evacuation.html","aea8a9c23dcfcb2500a8308484f73771"],["/android-chrome-192x192.png","4ad60aecb63bdfab4cad33b52d5097f2"],["/android-chrome-512x512.png","f07c3c38312427db4a533936a6a575c9"],["/apple-touch-icon.png","1ce721896a851646d058a2c2225d18df"],["/assets/img/img1.png","4bd05bdc10202ede34cfc6cba93bde80"],["/assets/img/img2.png","1bd892e8f1d3ef034553595b156db1fd"],["/assets/svg/amsf.svg","f377674da2d68bfd2eca84c215a0cd6d"],["/assets/svg/heading-image-example.svg","97f9ed1a1221d5353362b35a991414f3"],["/data-quest-guided-project.html","b36551ca18ff3fcb3dbd79daa59177c0"],["/data/index.html","df34144dcf57bc8f21c8d6a23eff1d12"],["/doctor-proportion.html","ba961bb33697282f2c7f83ebb5318d5c"],["/favicon-16x16.png","7ba2d2b346ba3444dbd8c6eecab17c3b"],["/favicon-32x32.png","bfd2baf3cc94a977966f24b719d1375a"],["/favicon.png","f9ebc5b37a6d03e75d12097ca919561c"],["/favicon.svg","d421f54bfbe396a2c122c374a3edb77c"],["/index.html","5f6cda817530c8b405e01c7bd090e2ae"],["/logo.png","fd2c38f87bed0987a03c6eac12e92824"],["/mask-icon.svg","042604fd5e8bdfa5d9ac053a8485e10b"],["/medical/index.html","2cf85124f263bfeca61c33a431861dbd"],["/news/index.html","6f5589d1d6d71d1926942533c91e4286"],["/setting-up-zsh.html","20977d2d883de62a8e789584d64f21d0"]],cacheName="sw-precache-v3-almace-scaffolding-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,t){var a=new URL(e);return"/"===a.pathname.slice(-1)&&(a.pathname+=t),a.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then((function(t){return new Response(t,{headers:e.headers,status:e.status,statusText:e.statusText})})):Promise.resolve(e)},createCacheKey=function(e,t,a,n){var c=new URL(e);return n&&c.pathname.match(n)||(c.search+=(c.search?"&":"")+encodeURIComponent(t)+"="+encodeURIComponent(a)),c.toString()},isPathWhitelisted=function(e,t){if(0===e.length)return!0;var a=new URL(t).pathname;return e.some((function(e){return a.match(e)}))},stripIgnoredUrlParameters=function(e,t){var a=new URL(e);return a.hash="",a.search=a.search.slice(1).split("&").map((function(e){return e.split("=")})).filter((function(e){return t.every((function(t){return!t.test(e[0])}))})).map((function(e){return e.join("=")})).join("&"),a.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map((function(e){var t=e[0],a=e[1],n=new URL(t,self.location),c=createCacheKey(n,hashParamName,a,!1);return[n.toString(),c]})));function setOfCachedUrls(e){return e.keys().then((function(e){return e.map((function(e){return e.url}))})).then((function(e){return new Set(e)}))}self.addEventListener("install",(function(e){e.waitUntil(caches.open(cacheName).then((function(e){return setOfCachedUrls(e).then((function(t){return Promise.all(Array.from(urlsToCacheKeys.values()).map((function(a){if(!t.has(a)){var n=new Request(a,{credentials:"same-origin"});return fetch(n).then((function(t){if(!t.ok)throw new Error("Request for "+a+" returned a response with status "+t.status);return cleanResponse(t).then((function(t){return e.put(a,t)}))}))}})))}))})).then((function(){return self.skipWaiting()})))})),self.addEventListener("activate",(function(e){var t=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then((function(e){return e.keys().then((function(a){return Promise.all(a.map((function(a){if(!t.has(a.url))return e.delete(a)})))}))})).then((function(){return self.clients.claim()})))})),self.addEventListener("fetch",(function(e){if("GET"===e.request.method){var t,a=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(t=urlsToCacheKeys.has(a))||(a=addDirectoryIndex(a,"index.html"),t=urlsToCacheKeys.has(a));0,t&&e.respondWith(caches.open(cacheName).then((function(e){return e.match(urlsToCacheKeys.get(a)).then((function(e){if(e)return e;throw Error("The cached response that was expected is missing.")}))})).catch((function(t){return fetch(e.request)})))}}));