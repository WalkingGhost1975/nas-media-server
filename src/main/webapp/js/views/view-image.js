NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _){
  Views.ImageCatalogView = Views.CatalogView.extend({
      template : '#template-image-catalog'
  });

  Views.ImageView = Marionette.ItemView.extend({
      tagName : 'li',
      className : 'span2',
      template : '#template-image',
  });

  Views.ImagesView = Marionette.CollectionView.extend({
      tagName : 'ul',
      className : 'thumbnails',
      itemView : Views.ImageView,
      itemViewContainer : '#content',
  });
});

