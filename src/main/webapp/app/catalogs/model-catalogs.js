NasMediaApp.module('Catalogs', function(Catalogs, App, Backbone, Marionette, $, _) {
    Catalogs.Catalog = Backbone.Model.extend({
        defaults: {
            active: false,
            category: '',
            name: ''
        },
        initialize : function()  {
            this.set({path : this.path()});
        },
        path : function() {
            return this.get('category') + "/" + this.get('name');
        }
    });

    Catalogs.CatalogCollection = Backbone.Collection.extend({
        initialize : function()  {
            this.on('clear:active', function()  {
                this.each(function(catalog) {
                    catalog.set({active:false},{silent:true});
                });
            });
        }
    });
});