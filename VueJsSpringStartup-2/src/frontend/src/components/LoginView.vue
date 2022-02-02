<template>
    <div>
        <form v-on:submit.prevent="login">
            <input v-model="username" placeholder="username">
            <input v-model="password" placeholder="password">
            <input type="submit" value="Login In">
        </form>
        <p>{{msg}}</p>
    </div>
</template>

<script>
    export default {
        name: "LoginView",
        data() {
            return {
                username: "",
                password: "",
                msg: "",
            }
        },
        methods: {
            login() {
                const requestOptions = {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username: this.username, password: this.password })
                };
                fetch("/api/messages/login", requestOptions)
                    .then(response => response.text())
                    .then(data => {
                        this.msg = data;
                    });
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>