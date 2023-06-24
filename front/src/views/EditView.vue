<script setup lang="ts">

import {useRouter} from "vue-router";
import axios from "axios";
import {ref} from "vue";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: ""
});

axios.get(`/jwlog/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/jwlog/posts/${props.postId}`, post.value).then(() => {
    router.replace({name: "home"});
  });
};
</script>

<template>
  <div>
    <el-input v-model="post.title" type="text"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정 완료</el-button>
  </div>
</template>

<style></style>
