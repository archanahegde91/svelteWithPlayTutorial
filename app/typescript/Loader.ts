import '../stylesheets/main.scss';

// @ts-ignore
import App from '../svelte/App.svelte';

const app = document.querySelector('#app');
if (app !== null) {
    new App({
        target: app
    });
}


