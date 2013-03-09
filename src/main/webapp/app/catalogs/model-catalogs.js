NasMediaApp.module('Catalogs', function(Catalogs, App, Backbone, Marionette, $, _) {
    Catalogs.Catalog = Backbone.Model.extend({
        defaults: {
            active: false,
            category: '',
            name: ''
        },
        initialize : function()  {
            this.set({path : this._path()});
        },
        _path : function() {
            return this.get('category') + "/" + this.get('name');
        }
    });

    Catalogs.CatalogCollection = Backbone.Collection.extend({
        initialize : function()  {
            this.on('change:active', function(model)  {
                this.deselectCatalogs(model);
            });
        },
        deselectCatalogs : function(activeCatalog) {
            this.chain().reject(function(catalog)  {
                return catalog === activeCatalog;
            }).each(function(catalog) {
                catalog.set({active:false},{silent:true});
            });
        }
    });

    Catalogs.Node = Backbone.Model.extend({
    });

    Catalogs.NodeCollection = Backbone.Collection.extend({
        model : Catalogs.Node
    });

    Catalogs.Folder = Backbone.Model.extend({
    });

    Catalogs.FolderCollection = Backbone.Collection.extend({
        model : Catalogs.Folder
    });

});