NasMediaApp.module('Music', function(Music, App, Backbone, Marionette, $, _) {

    Music.AudioFile = Backbone.Model.extend({
        defaults: {
        },
        initialize: function() {
        }
    });

    Music.MusicCatalog = NasMediaApp.Catalogs.Catalog.extend({
        defaults: {
            category: 'music'
        }
    });

    Music.MusicCatalogCollection = NasMediaApp.Catalogs.CatalogCollection.extend({
        url: 'repo/audio',
        model: Music.MusicCatalog
    });

});

