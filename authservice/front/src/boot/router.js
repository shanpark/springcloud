import { boot } from "quasar/wrappers";
import { axios } from "boot/axios";
import { useUserStore } from "stores/user";

const LOGIN_PATH = "/login";
const HOME_PATH = "/";
const PERMIT_ALL = [LOGIN_PATH]; // 모든 사용자 접근 허용 가능 화면 선언.

// more info on params: https://v2.quasar.dev/quasar-cli/boot-files
export default boot(({ router }) => {
  const userStore = useUserStore();
  router.beforeEach((to, from, next) => {
    // 이미 로그인 상태이면
    if (userStore.isLogin) {
      // 로그인 페이지로 이동하려고 하면
      if (to.path == LOGIN_PATH) {
        next({ path: HOME_PATH }); // home 페이지로 리다이렉트 시킨다.
      } else {
        next();
      }
    } else {
      // JWT가 쿠키로 내려오기 때문에 새로고침이나 새로 탭을 열었을 때 userStore.userVo는 null로 초기화될 것이다.
      // 따라서 아래 요청을 새로 보내서 성공적이라면 정상적인 JWT 쿠키가 이미 있는 것이고 다시 사용자 정보를 저장해 놓으면 된다.
      // 만약 실패한다면 JWT 쿠키가 없는 것이므로 아직 인증받지 못했다는 것이므로 login 페이지로 이동시켜주면 된다.
      axios
        .get("/auth/relogin")
        .then((res) => {
          // TODO 인증 규격에 맞게 변경한다.
          userStore.setUser(res.data.user); // AuthResDto 객체가 내려올 것이다.
          if (to.path == LOGIN_PATH) {
            next({ path: HOME_PATH }); // home 페이지로 리다이렉트 시킨다.
          } else {
            next(); // 성공하면 계속.
          }
        })
        .catch((err) => {
          if (PERMIT_ALL.includes(to.path)) {
            next(); // 그냥 이동시킨다.
          } else {
            console.log(err);
            next({ path: LOGIN_PATH }); // 실패 시 login 페이지로 이동.
          }
        });
    }
  });
});
