NasMediaApp.module('NasMediaController', function(NasMediaController, App, Backbone, Marionette, $, _) {

    NasMediaController.Router = Marionette.AppRouter.extend({
        appRoutes: {
            'home': 'showHome',
            'music': 'showMusic',
            'images': 'showImages',
            'music/:catalog': 'showMusic',
            'images/:catalog': 'showImages',
            'music/:catalog/*musicPath': 'showMusic',
            'images/:catalog/*imagePath': 'showImages'
        }
    });

    controller = {
        pages: {
            'home': App.Layout.Home,
            'music': App.Layout.Music,
            'images': App.Layout.Images
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
        showMusic: function(catalog, path) {
            this.model.set({'page': 'music', 'catalog': catalog, 'path': path});
            this.displayMediaPlayer();
        },
        showImages: function(catalog, path) {
            this.model.set({'page': 'images', 'catalog': catalog, 'path': path});
            this.displayMediaPlayer();
        },
        displayMediaPlayer: function() {
            var page = this.model.get('page');
            var layout = new this.pages[page];
            layout.model = this.model;
            App.content.show(layout);
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

