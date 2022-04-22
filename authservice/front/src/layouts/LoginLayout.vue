<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>Quasar App</q-toolbar-title>
        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <q-page>
        <q-card class="login-container fixed-center">
          <q-card-section>
            <q-input
              type="email"
              v-model="email"
              label="이메일을 입력하세요"
              maxlength="256"
            ></q-input>
            <q-input
              type="password"
              v-model="password"
              label="패스워드를 입력하세요"
              maxlength="64"
            ></q-input>
          </q-card-section>

          <q-card-actions class="q-px-md">
            <q-btn
              color="primary"
              class="full-width text-body1 text-weight-bold"
              @click="login"
              >로그인</q-btn
            >
            <a href="/oauth2/authorization/google" class="full-width">
              <q-btn
                color="primary"
                class="full-width text-body1 text-weight-bold"
                >Sign-in with Google</q-btn
              >
            </a>
            <a href="/oauth2/authorization/naver" class="full-width">
              <q-btn
                color="primary"
                class="full-width text-body1 text-weight-bold"
                >Sign-in with Naver</q-btn
              >
            </a>
            <a href="/oauth2/authorization/kakao" class="full-width">
              <q-btn
                color="primary"
                class="full-width text-body1 text-weight-bold"
                >Sign-in with Kakao</q-btn
              >
            </a>
          </q-card-actions>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { ref } from "vue";
import { useQuasar } from "quasar";
import { useRouter } from "vue-router";
import { useUserStore } from "stores/user";
import { axios } from "boot/axios";

export default {
  setup() {
    const $q = useQuasar();
    const $router = useRouter();
    const userStore = useUserStore();

    const email = ref("");
    const password = ref("");

    function showSnackbar(type, message) {
      $q.notify({ type, message });
    }

    function validateForm() {
      if (!email.value) {
        showSnackbar(
          "negative",
          "이메일을 입력하지 않았거나 형식이 맞지 않습니다."
        );
        return false;
      }

      if (!password.value) {
        showSnackbar(
          "negative",
          "비밀번호를 입력하지 않았거나 형식이 맞지 않습니다."
        );
        return false;
      }

      return true;
    }

    function login() {
      if (!validateForm()) return;

      axios
        .post("/auth/login", {
          userId: email.value,
          password: password.value,
        })
        .then((res) => {
          // 200이 내려오면 항상 성공한 것이다. code를 사용할 일이 없다.
          userStore.setUser(res.data.user);
          $router.push({ path: "/" });
        })
        .catch((err) => {
          // 다른 API와 달리 인증 실패시 401이 내려온다.
          userStore.setUser(null);
          showSnackbar(
            "negative",
            "이메일이나 비밀번호가 유효하지 않습니다. 다시 확인해주세요."
          );
        });
    }

    return {
      email,
      password,
      login,
    };
  },
};
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  max-width: 400px;
}
</style>
