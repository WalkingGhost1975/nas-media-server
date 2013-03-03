NasMediaApp.module('Music', function(Music, App, Backbone, Marionette, $, _){

  Music.AudioFile = Backbone.Model.extend({
    defaults: {
    },

    initialize : function() {
    }
  });

  Music.MusicCatalogCollection = NasMediaApp.Catalogs.CatalogCollection.extend({
      url : 'repo/audio'
  });

});

