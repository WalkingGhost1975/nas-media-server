NasMediaApp.module('NasMediaController', function(NasMediaController, App, Backbone, Marionette, $, _) {

    NasMediaController.Router = Marionette.AppRouter.extend({
        appRoutes: {
            'home': 'showHome',
            'audio': 'showAudio',
            'images': 'showImages',
            'audio/:catalog': 'showAudio',
            'images/:catalog': 'showImages',
            'audio/:catalog/*audioPath': 'showAudio',
            'images/:catalog/*imagePath': 'showImages'
        }
    });

    controller = {
        pages: {
            'home': App.Layout.Home,
            'audio': App.Views.AudioLayout,
            'images': App.Views.ImagesLayout
        },
        // Start the app by showing the appropriate views
        start: function() {
            this.model = new App.Model.MediaModel();
            this.showNavigation();
            this.displayMediaPlayer();
        },
        showNavigation: function() {
            var navLayout = new App.Layout.Navigation({
                model: this.model
            });
            App.navigation.show(navLayout);
        },
        showHome: function() {
            this.model.set({'page': 'home'});
            this.displayMediaPlayer();
        },
        showAudio: function(catalog, path) {
            this.model.catalogs = new App.Audio.AudioCatalogCollection();
            this._configureMediaModel('audio',catalog, path);
            this.displayMediaPlayer();
        },
        showImages: function(catalog, path) {
            this.model.catalogs = new App.Images.ImageCatalogCollection();
            this._configureMediaModel('images',catalog, path);
            this.displayMediaPlayer();
        },
        displayMediaPlayer: function() {
            var page = this.model.get('page');
            var layout = new this.pages[page]({
                model: this.model
            });
            App.content.show(layout);
        },
        _configureMediaModel: function(page, catalog, path) {
            //First set page and catalog
            this.model.set({'page': page, 'catalog': catalog});
            //Second set path
            this.model.set({'path': path});
            this.model.listenTo(this.model.catalogs, 'change:active', this.model.syncFromCatalogCollection);
            this.model.listenTo(this.model.catalogs, 'sync', this.model.syncToCatalogCollection);
        }
    };

    NasMediaController.addInitializer(function() {
        NasMediaRouter = new NasMediaController.Router({
            controller: controller
        });
        controller.start();
    });

    $(document).on("click", "a:not([data-bypass])", function(evt) {
        // Get the anchor href and protcol
        var href = $(this).attr("href");
        var protocol = this.protocol + "//";

        // Ensure the protocol is not part of URL, meaning its relative.
        if (href && href.slice(0, protocol.length) !== protocol &&
                href.indexOf("javascript:") !== 0) {
            evt.preventDefault();
            Backbone.history.navigate(href);
        }
    });
});

