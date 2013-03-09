NasMediaApp.module('Layout', function(Layout, App, Backbone, Marionette, $, _) {

    Layout.Navigation = Marionette.Layout.extend({
        template: '#template-navigation-view',
        ui: {
            search: '#search',
            navItems: 'ul.nav li'
        },
        modelEvents: {
            'change:page': 'render'
        },
        events: {
            'keypress #search': 'onInputKeypress'
        },
        initialize: function() {
        },
        onRender: function() {
            var page = this.model.get('page');
            if (page) {
                this.ui.navItems.removeClass('active').filter('#' + page).addClass('active');
            }
        },
        onInputKeypress: function(evt) {
            var ENTER_KEY = 13;
            var todoText = this.ui.search.val().trim();

            if (evt.which === ENTER_KEY && todoText) {
                alert("Search Triggered!");
            }
        }
    });

    Layout.Home = Marionette.Layout.extend({
        template: '#template-home-view',
        initialize: function() {
        },
        ui: {
        },
        onRender: function() {
            this.$el.find('.carousel').carousel({
                interval: 3000,
                cycle: true
            });
        },
        events: {
        }
    });

});