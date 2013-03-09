NasMediaApp.module('Model', function(Model, App, Backbone, Marionette, $, _){

  Model.MediaModel = Backbone.Model.extend({
    defaults: {
        page : 'home',
        catalog : '',
        path : ''
    },

    initialize : function() {
    }
  });

});