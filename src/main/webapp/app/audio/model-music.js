NasMediaApp.module('Music', function(Music, App, Backbone, Marionette, $, _) {

    Music.AudioFile = Backbone.Model.extend({
        defaults: {
        },
        initialize: function() {
        }
    });

    Music.MusicCatalog = App.Catalogs.Catalog.extend({
        defaults: {
            category: 'audio'
        }
    });

    Music.MusicCatalogCollection = App.Catalogs.CatalogCollection.extend({
        url: 'repo/audio',
        model: Music.MusicCatalog
    });

});

