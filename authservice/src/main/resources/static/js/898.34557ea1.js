"use strict";(self["webpackChunkauthservice"]=self["webpackChunkauthservice"]||[]).push([[898],{898:(e,a,t)=>{t.r(a),t.d(a,{default:()=>T});var l=t(9835),o=t(6970);const u=(0,l.Uk)("Quasar App"),n=(0,l.Uk)("로그인"),i={href:"/oauth2/authorization/google",class:"full-width"},r=(0,l.Uk)("Sign-in with Google"),s={href:"/oauth2/authorization/naver",class:"full-width"},d=(0,l.Uk)("Sign-in with Naver"),c={href:"/oauth2/authorization/kakao",class:"full-width"},p=(0,l.Uk)("Sign-in with Kakao");function h(e,a,t,h,w,f){const m=(0,l.up)("q-toolbar-title"),g=(0,l.up)("q-toolbar"),v=(0,l.up)("q-header"),b=(0,l.up)("q-input"),_=(0,l.up)("q-card-section"),y=(0,l.up)("q-btn"),k=(0,l.up)("q-card-actions"),x=(0,l.up)("q-card"),W=(0,l.up)("q-page"),q=(0,l.up)("q-page-container"),Q=(0,l.up)("q-layout");return(0,l.wg)(),(0,l.j4)(Q,{view:"hHh lpR fFf"},{default:(0,l.w5)((()=>[(0,l.Wm)(v,{elevated:"",class:"bg-primary text-white"},{default:(0,l.w5)((()=>[(0,l.Wm)(g,null,{default:(0,l.w5)((()=>[(0,l.Wm)(m,null,{default:(0,l.w5)((()=>[u])),_:1}),(0,l._)("div",null,"Quasar v"+(0,o.zw)(e.$q.version),1)])),_:1})])),_:1}),(0,l.Wm)(q,null,{default:(0,l.w5)((()=>[(0,l.Wm)(W,null,{default:(0,l.w5)((()=>[(0,l.Wm)(x,{class:"login-container fixed-center"},{default:(0,l.w5)((()=>[(0,l.Wm)(_,null,{default:(0,l.w5)((()=>[(0,l.Wm)(b,{type:"email",modelValue:h.email,"onUpdate:modelValue":a[0]||(a[0]=e=>h.email=e),label:"이메일을 입력하세요",maxlength:"256"},null,8,["modelValue"]),(0,l.Wm)(b,{type:"password",modelValue:h.password,"onUpdate:modelValue":a[1]||(a[1]=e=>h.password=e),label:"패스워드를 입력하세요",maxlength:"64"},null,8,["modelValue"])])),_:1}),(0,l.Wm)(k,{class:"q-px-md"},{default:(0,l.w5)((()=>[(0,l.Wm)(y,{color:"primary",class:"full-width text-body1 text-weight-bold",onClick:h.login},{default:(0,l.w5)((()=>[n])),_:1},8,["onClick"]),(0,l._)("a",i,[(0,l.Wm)(y,{color:"primary",class:"full-width text-body1 text-weight-bold"},{default:(0,l.w5)((()=>[r])),_:1})]),(0,l._)("a",s,[(0,l.Wm)(y,{color:"primary",class:"full-width text-body1 text-weight-bold"},{default:(0,l.w5)((()=>[d])),_:1})]),(0,l._)("a",c,[(0,l.Wm)(y,{color:"primary",class:"full-width text-body1 text-weight-bold"},{default:(0,l.w5)((()=>[p])),_:1})])])),_:1})])),_:1})])),_:1})])),_:1})])),_:1})}var w=t(499),f=t(9302),m=t(8910),g=t(6144),v=t(1569);const b={setup(){const e=(0,f.Z)(),a=(0,m.tv)(),t=(0,g.L)(),l=(0,w.iH)(""),o=(0,w.iH)("");function u(a,t){e.notify({type:a,message:t})}function n(){return l.value?!!o.value||(u("negative","비밀번호를 입력하지 않았거나 형식이 맞지 않습니다."),!1):(u("negative","이메일을 입력하지 않았거나 형식이 맞지 않습니다."),!1)}function i(){n()&&v.axios.post("/auth/login",{userId:l.value,password:o.value}).then((e=>{t.setUser(e.data.user),a.push({path:"/"})})).catch((e=>{t.setUser(null),u("negative","이메일이나 비밀번호가 유효하지 않습니다. 다시 확인해주세요.")}))}return{email:l,password:o,login:i}}};var _=t(1639),y=t(7605),k=t(6602),x=t(1663),W=t(1973),q=t(2133),Q=t(9885),Z=t(4458),U=t(3190),C=t(2916),V=t(1821),z=t(963),H=t(9984),S=t.n(H);const I=(0,_.Z)(b,[["render",h],["__scopeId","data-v-04d3b396"]]),T=I;S()(b,"components",{QLayout:y.Z,QHeader:k.Z,QToolbar:x.Z,QToolbarTitle:W.Z,QPageContainer:q.Z,QPage:Q.Z,QCard:Z.Z,QCardSection:U.Z,QInput:C.Z,QCardActions:V.Z,QBtn:z.Z})}}]);