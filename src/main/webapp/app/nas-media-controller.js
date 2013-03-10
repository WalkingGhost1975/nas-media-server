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
            'audio': App.Views.AudioPageLayout,
            'images': App.Views.ImagesPageLayout
        },
        // Start the app by showing the appropriate views
        start: function() {
            this.model = new App.Model.LocationModel();
            this.showHome();
        },
        showNavigation: function() {
            var navLayout = new App.Layout.Navigation({
                model: this.model
            });
            App.navigation.show(navLayout);
        },
        showHome: function() {
            this.model.set({'page': 'home'});
            this.showNavigation();
            this.displayMediaPlayer();
        },
        showAudio: function(catalog, path) {
            this._setupLocationModel('audio',catalog, path);
            this.showNavigation();
            this.displayMediaPlayer();
        },
        showImages: function(catalog, path) {
            this._setupLocationModel('images',catalog, path);
            this.showNavigation();
            this.displayMediaPlayer();
        },
        displayMediaPlayer: function() {
            var page = this.model.get('page');
            var layout = new this.pages[page]({
                model: this.model
            });
            App.content.show(layout);
        },
        _setupLocationModel: function(page, catalog, path) {
            this.model.set({'page': page, 'catalog': catalog,'path': path},{silent:true});
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

