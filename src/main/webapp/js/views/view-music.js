NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _){
  Views.MusicCatalogView = Views.CatalogView.extend({
      template : '#template-music-catalog'
  });

  Views.AudioFileView = Marionette.ItemView.extend({
      tagName : 'tr',
      template : '#template-audio-file'
  });

  Views.AudioFilesView = Marionette.CollectionView.extend({
      tagName : '',
      itemView : Views.AudioFileView,
      itemViewContainer : '#content'
  });

});

