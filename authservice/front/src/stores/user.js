import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    userVo: null,
  }),
  getters: {
    isLogin: (state) => state.userVo != null,
  },
  actions: {
    setUser(userVo) {
      this.userVo = userVo;
    },
  },
});
