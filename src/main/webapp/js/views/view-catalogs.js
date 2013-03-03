NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _){
  Views.CatalogView = Marionette.ItemView.extend({
      tagName : 'li'
  });

  Views.CatalogsView = Marionette.CollectionView.extend({
      tagName : 'ul',
      className : 'unstyled',
      itemView : Views.CatalogView,
      initialize : function() {
        this.collection.fetch();
      }
  });
});
