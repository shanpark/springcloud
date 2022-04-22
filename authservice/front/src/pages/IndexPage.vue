<template>
  <q-page class="flex flex-center">{{ message }}</q-page>
</template>

<script>
import { defineComponent, ref } from "vue";
import { useQuasar } from "quasar";
import { axios } from "boot/axios";

/**
 * 간단한 Vue3용 Page 샘플
 */
export default defineComponent({
  name: "IndexPage",

  setup() {
    const $q = useQuasar();

    let message = ref("Hello.");

    function showSnackbar(type, message) {
      $q.notify({ type, message });
    }

    axios
      .post("/rest/sample", {})
      .then((res) => {
        message.value = res.data.message;
      })
      .catch((err) => {
        showSnackbar("negative", "서버로 요청이 실했습니다.");
      });

    return {
      message,
    };
  },
});
</script>
