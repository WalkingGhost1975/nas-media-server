NasMediaApp.module('Audio', function(Audio, App, Backbone, Marionette, $, _) {

    Audio.AudioFile = Backbone.Model.extend({
        defaults: {
        },
        initialize: function() {
        }
    });

    Audio.AudioCatalog = App.Catalogs.Catalog.extend({
        defaults: {
            category: 'audio'
        }
    });

    Audio.AudioCatalogCollection = App.Catalogs.CatalogCollection.extend({
        url: 'repo/audio',
        model: Audio.AudioCatalog
    });

});

