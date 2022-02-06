<template>
  <div>
    <app-header></app-header>
    <router-view></router-view>
  </div>
</template>

<script>
  import HeaderView from "./components/HeaderView";

  export default {
    name: 'App',
    components: {
      'app-header': HeaderView
    }
  }
</script>

<style>
</style>
