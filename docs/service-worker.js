"use strict";var precacheConfig=[["/404.html","f0e611127d3aaaa6856314b6f6895750"],["/Almace-Scaffolding-quick-post.html","13e72f6d8e4f12e6fdc1a2d377db19d8"],["/Dataquest-guided-project-3.html","7d84a904d88c0c69b4c23a74045569bc"],["/Displaying-math-formula-on-ASMF.html","fa64c5b1f294a1169ed3abbbef60ca28"],["/Guided-project-Mobile-App-for-lottery-addiction.html","c89e8de480cded76be01a380fd1c6127"],["/Proportion-of-Doctors-in-Indonesia.html","939eccfdb6969861b6f2e61f834be418"],["/Reviewing-ECG.html","c9fc4e03e0ce366e9e0f74c1d405565b"],["/about/index.html","cd0bae0e62e8bbdc78919b13db151beb"],["/air-medical-evacuation.html","aea8a9c23dcfcb2500a8308484f73771"],["/android-chrome-192x192.png","4ad60aecb63bdfab4cad33b52d5097f2"],["/android-chrome-512x512.png","f07c3c38312427db4a533936a6a575c9"],["/apple-touch-icon.png","1ce721896a851646d058a2c2225d18df"],["/assets/img/img1.png","4bd05bdc10202ede34cfc6cba93bde80"],["/assets/img/img2.png","1bd892e8f1d3ef034553595b156db1fd"],["/assets/svg/amsf.svg","f377674da2d68bfd2eca84c215a0cd6d"],["/assets/svg/heading-image-example.svg","97f9ed1a1221d5353362b35a991414f3"],["/data-quest-guided-project.html","b36551ca18ff3fcb3dbd79daa59177c0"],["/data/index.html","ffc56b7e6fb95168cfc4a0f5a83b42d6"],["/doctor-proportion.html","ba961bb33697282f2c7f83ebb5318d5c"],["/favicon-16x16.png","7ba2d2b346ba3444dbd8c6eecab17c3b"],["/favicon-32x32.png","bfd2baf3cc94a977966f24b719d1375a"],["/favicon.png","f9ebc5b37a6d03e75d12097ca919561c"],["/favicon.svg","d421f54bfbe396a2c122c374a3edb77c"],["/index.html","de3b82613d9e255aab6c7b302907eebe"],["/logo.png","fd2c38f87bed0987a03c6eac12e92824"],["/mask-icon.svg","042604fd5e8bdfa5d9ac053a8485e10b"],["/medical/index.html","497ee981d4324f3201e067e6ad735acf"],["/news/index.html","edb9dacb5a2aa3267805f309ea6f7195"],["/setting-up-zsh.html","20977d2d883de62a8e789584d64f21d0"]],cacheName="sw-precache-v3-almace-scaffolding-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,a){var t=new URL(e);return"/"===t.pathname.slice(-1)&&(t.pathname+=a),t.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then((function(a){return new Response(a,{headers:e.headers,status:e.status,statusText:e.statusText})})):Promise.resolve(e)},createCacheKey=function(e,a,t,n){var c=new URL(e);return n&&c.pathname.match(n)||(c.search+=(c.search?"&":"")+encodeURIComponent(a)+"="+encodeURIComponent(t)),c.toString()},isPathWhitelisted=function(e,a){if(0===e.length)return!0;var t=new URL(a).pathname;return e.some((function(e){return t.match(e)}))},stripIgnoredUrlParameters=function(e,a){var t=new URL(e);return t.hash="",t.search=t.search.slice(1).split("&").map((function(e){return e.split("=")})).filter((function(e){return a.every((function(a){return!a.test(e[0])}))})).map((function(e){return e.join("=")})).join("&"),t.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map((function(e){var a=e[0],t=e[1],n=new URL(a,self.location),c=createCacheKey(n,hashParamName,t,!1);return[n.toString(),c]})));function setOfCachedUrls(e){return e.keys().then((function(e){return e.map((function(e){return e.url}))})).then((function(e){return new Set(e)}))}self.addEventListener("install",(function(e){e.waitUntil(caches.open(cacheName).then((function(e){return setOfCachedUrls(e).then((function(a){return Promise.all(Array.from(urlsToCacheKeys.values()).map((function(t){if(!a.has(t)){var n=new Request(t,{credentials:"same-origin"});return fetch(n).then((function(a){if(!a.ok)throw new Error("Request for "+t+" returned a response with status "+a.status);return cleanResponse(a).then((function(a){return e.put(t,a)}))}))}})))}))})).then((function(){return self.skipWaiting()})))})),self.addEventListener("activate",(function(e){var a=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then((function(e){return e.keys().then((function(t){return Promise.all(t.map((function(t){if(!a.has(t.url))return e.delete(t)})))}))})).then((function(){return self.clients.claim()})))})),self.addEventListener("fetch",(function(e){if("GET"===e.request.method){var a,t=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(a=urlsToCacheKeys.has(t))||(t=addDirectoryIndex(t,"index.html"),a=urlsToCacheKeys.has(t));0,a&&e.respondWith(caches.open(cacheName).then((function(e){return e.match(urlsToCacheKeys.get(t)).then((function(e){if(e)return e;throw Error("The cached response that was expected is missing.")}))})).catch((function(a){return fetch(e.request)})))}}));