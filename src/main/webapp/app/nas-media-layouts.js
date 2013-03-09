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

    Layout.Music = Marionette.Layout.extend({
        template: '#template-music-view',
        regions: {
            sidebar: '#sidebar',
            content: '#content'
        },
        initialize: function() {
            //Prepare view
            this.catalogsView = new App.Views.CatalogsView({
                itemView: App.Views.MusicCatalogView,
                collection: this.model.catalogs
            });
        },

        onRender: function() {
            this.sidebar.show(this.catalogsView);
        }
    });

    Layout.Images = Marionette.Layout.extend({
        template: '#template-image-view',
        regions: {
            sidebar: '#sidebar',
            content: '#content'
        },
        initialize: function() {
            this.contentView = new App.Views.ImagesView();

            this.catalogsView = new App.Views.CatalogsView({
                itemView: App.Views.ImageCatalogView,
                collection: this.model.catalogs
            });
        },
        onRender: function() {
            this.content.show(this.contentView);
            this.sidebar.show(this.catalogsView);
        }
    });

});