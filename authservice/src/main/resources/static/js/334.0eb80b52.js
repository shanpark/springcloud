"use strict";(self["webpackChunkauthservice"]=self["webpackChunkauthservice"]||[]).push([[334],{7334:(e,t,n)=>{n.r(t),n.d(t,{default:()=>V});var l=n(9835),a=n(6970);const u=(0,l.Uk)("Quasar App"),o=(0,l.Uk)("Essential Links"),i=(0,l.Uk)("Logout");function r(e,t,n,r,s,c){const p=(0,l.up)("q-btn"),d=(0,l.up)("q-toolbar-title"),m=(0,l.up)("q-toolbar"),w=(0,l.up)("q-header"),f=(0,l.up)("q-item-label"),k=(0,l.up)("EssentialLink"),g=(0,l.up)("q-icon"),v=(0,l.up)("q-item-section"),q=(0,l.up)("q-item"),Z=(0,l.up)("q-list"),h=(0,l.up)("q-drawer"),W=(0,l.up)("router-view"),_=(0,l.up)("q-page-container"),Q=(0,l.up)("q-layout");return(0,l.wg)(),(0,l.j4)(Q,{view:"lHh Lpr lFf"},{default:(0,l.w5)((()=>[(0,l.Wm)(w,{elevated:""},{default:(0,l.w5)((()=>[(0,l.Wm)(m,null,{default:(0,l.w5)((()=>[(0,l.Wm)(p,{flat:"",dense:"",round:"",icon:"menu","aria-label":"Menu",onClick:e.toggleLeftDrawer},null,8,["onClick"]),(0,l.Wm)(d,null,{default:(0,l.w5)((()=>[u])),_:1}),(0,l._)("div",null,"Quasar v"+(0,a.zw)(e.$q.version),1)])),_:1})])),_:1}),(0,l.Wm)(h,{modelValue:e.leftDrawerOpen,"onUpdate:modelValue":t[0]||(t[0]=t=>e.leftDrawerOpen=t),"show-if-above":"",bordered:""},{default:(0,l.w5)((()=>[(0,l.Wm)(Z,null,{default:(0,l.w5)((()=>[(0,l.Wm)(f,{header:""},{default:(0,l.w5)((()=>[o])),_:1}),((0,l.wg)(!0),(0,l.iD)(l.HY,null,(0,l.Ko)(e.essentialLinks,(e=>((0,l.wg)(),(0,l.j4)(k,(0,l.dG)({key:e.title},e),null,16)))),128)),(0,l.Wm)(q,{clickable:"",onClick:e.logout},{default:(0,l.w5)((()=>[(0,l.Wm)(v,{avatar:""},{default:(0,l.w5)((()=>[(0,l.Wm)(g,{name:"logout"})])),_:1}),(0,l.Wm)(v,null,{default:(0,l.w5)((()=>[(0,l.Wm)(f,null,{default:(0,l.w5)((()=>[i])),_:1})])),_:1})])),_:1},8,["onClick"])])),_:1})])),_:1},8,["modelValue"]),(0,l.Wm)(_,null,{default:(0,l.w5)((()=>[(0,l.Wm)(W)])),_:1})])),_:1})}function s(e,t,n,u,o,i){const r=(0,l.up)("q-icon"),s=(0,l.up)("q-item-section"),c=(0,l.up)("q-item-label"),p=(0,l.up)("q-item");return(0,l.wg)(),(0,l.j4)(p,{clickable:"",onClick:e.onClick},{default:(0,l.w5)((()=>[e.icon?((0,l.wg)(),(0,l.j4)(s,{key:0,avatar:""},{default:(0,l.w5)((()=>[(0,l.Wm)(r,{name:e.icon},null,8,["name"])])),_:1})):(0,l.kq)("",!0),(0,l.Wm)(s,null,{default:(0,l.w5)((()=>[(0,l.Wm)(c,null,{default:(0,l.w5)((()=>[(0,l.Uk)((0,a.zw)(e.title),1)])),_:1}),(0,l.Wm)(c,{caption:""},{default:(0,l.w5)((()=>[(0,l.Uk)((0,a.zw)(e.caption),1)])),_:1})])),_:1})])),_:1},8,["onClick"])}var c=n(8910);const p=(0,l.aZ)({name:"EssentialLink",props:{title:{type:String,required:!0},caption:{type:String,default:""},link:{type:String,default:"#"},icon:{type:String,default:""}},setup(e){const t=(0,c.tv)(),n=()=>{e.link.startsWith("http")?window.open(e.link,"_blank").focus():t.push(e.link)};return{onClick:n}}});var d=n(1639),m=n(490),w=n(1233),f=n(2857),k=n(3115),g=n(9984),v=n.n(g);const q=(0,d.Z)(p,[["render",s]]),Z=q;v()(p,"components",{QItem:m.Z,QItemSection:w.Z,QIcon:f.Z,QItemLabel:k.Z});var h=n(499),W=n(9302),_=n(6144),Q=n(1569);const b=[{title:"Docs",caption:"quasar.dev",icon:"school",link:"https://quasar.dev"}],L=(0,l.aZ)({name:"MainLayout",components:{EssentialLink:Z},setup(){const e=(0,W.Z)(),t=(0,c.tv)(),n=(0,_.L)(),l=(0,h.iH)(!1);function a(t,n){e.notify({type:t,message:n})}function u(){Q.axios.post("/auth/logout",{id:n.userVo.id}).then((e=>{n.setUser(null),t.push({path:"/login"})})).catch((e=>{console.log(e),a("negative","서버로 요청이 실패했습니다.")}))}return{essentialLinks:b,leftDrawerOpen:l,logout:u,toggleLeftDrawer(){l.value=!l.value}}}});var y=n(7605),C=n(6602),D=n(1663),I=n(963),U=n(1973),S=n(8673),j=n(3246),E=n(2133);const H=(0,d.Z)(L,[["render",r]]),V=H;v()(L,"components",{QLayout:y.Z,QHeader:C.Z,QToolbar:D.Z,QBtn:I.Z,QToolbarTitle:U.Z,QDrawer:S.Z,QList:j.Z,QItemLabel:k.Z,QItem:m.Z,QItemSection:w.Z,QIcon:f.Z,QPageContainer:E.Z})}}]);