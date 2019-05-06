<template>
  <v-app>
    <v-content>
      <CallSampleWebapi />
    </v-content>
  </v-app>
</template>

<script>
import CallSampleWebapi from "./components/CallSampleWebapi";

export default {
  name: "App",
  components: {
    CallSampleWebapi: CallSampleWebapi
  },
  data() {
    return {
      //
    };
  }
};
</script>
