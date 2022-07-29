<template>
  <v-app>

    <router-view></router-view>

  </v-app>
</template>

<script>

export default {
  name: 'App',

  data(){
      return{

      }
  },

    mounted: function() {
        let elHtml = document.getElementsByTagName('html')[0];
        elHtml.style.overflowY = 'auto'
    },
    destroyed: function() {
        let elHtml = document.getElementsByTagName('html')[0];
        elHtml.style.overflowY = null
    },
};
</script>

<style>
  #app{
    background: #f5f5f5;
  }
</style>