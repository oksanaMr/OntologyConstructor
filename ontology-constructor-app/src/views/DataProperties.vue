<template>
  <div class="dataProperty">
    <v-container>
        <h2 class="display-1 ml-5">Свойства данных</h2>

        <v-row>

            <v-col cols=4>
                <v-card>
                    <v-list style="maxHeight: 600px; overflow-y: auto;">
                        <v-subheader>СПИСОК СВОЙСТВ ДАННЫХ
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddProperty = true">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>
                            <v-list-item
                                v-for="(item,i) in properies"
                                :key="item.name"
                                v-on:click="selectProperty(i)"
                            >
                                <v-list-item-icon>
                                    <v-icon color="green darken-1" large>mdi-alpha-p-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.name"></v-list-item-title>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab v-on:click="deleteProperty(i)">
                                            <v-icon>mdi-delete</v-icon>
                                        </v-btn>
                                    </v-hover>
                                </v-list-item-action>
                            </v-list-item>
                    </v-list>
                </v-card>
            </v-col>

            <v-col cols=8>
                <v-card>
                    <v-card-title>Описание {{activeProperty.name}}</v-card-title>

                    <v-row no-gutters class="mt-3">
                        <v-col cols=2>
                            <v-subheader>Property IRI</v-subheader>
                        </v-col>
                        <v-col cols=7>
                            <v-text-field readonly v-model="activeProperty.iri"></v-text-field> 
                        </v-col>
                        <v-col cols=2>
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;green&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogEditIRIProperty = true; newIRIProperty = activeProperty.iri">
                                    <v-icon>mdi-pencil</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-col>
                    </v-row>

                    <v-divider></v-divider>

                    <v-list>
                        <v-subheader>Характиристика:</v-subheader>
                        <v-list-item-group multiple>
                            <v-list-item>
                                <v-list-item-action>
                                    <v-checkbox v-model="activeProperty.isFunctional" color="primary" @change="changeCharacter()"></v-checkbox>
                                </v-list-item-action>
                                <v-list-item-content>
                                    <v-list-item-title>Функциональное</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>

                    <v-divider></v-divider>
                    
                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Домены:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;domain&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.domains" :key="item">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;domain&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Диапазоны:
                           <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddRange=true; typeAxiom=&quot;range&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.ranges" :key="item">
                            <v-list-item-icon>
                                <v-icon color="red darken-3">mdi-alpha-r-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;range&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Эквивалентные свойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomProperty = true; typeAxiom=&quot;equivalent&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.equivalents" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;equivalent&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Непересекающиеся свойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomProperty=true; typeAxiom=&quot;disjoint&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.disjointProps" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;disjoint&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Суперсвойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomProperty = true; typeAxiom=&quot;superProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>
                        <v-list-item v-for="(item, i) in activeProperty.superProps" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;superProp&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Подсвойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomProperty = true; typeAxiom=&quot;subProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.subProps" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;subProp&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                </v-card>
            </v-col>

        </v-row>

        <v-dialog v-model="dialogAddProperty" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить новый свойство данных</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=2>
                                <v-subheader>Property IRI</v-subheader>
                            </v-col>
                            <v-col cols=9>
                                <v-text-field class="form-control" v-model="newProperty" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>      
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddProperty = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addNewProperty">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogEditIRIProperty" max-width="700px">
            <v-card>
                <v-card-title class="headline">Изменить IRI свойства</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=3>
                                <v-subheader>New Property IRI</v-subheader>
                            </v-col>
                            <v-col cols=8>
                                <v-text-field class="form-control" v-model="newIRIProperty" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn  color="primary" text @click="dialogEditIRIProperty = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="editIRIProperty">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddAxiomProperty" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить аксиому для свойства</v-card-title>
    
                   <v-list>
                        <v-subheader>Свойства данных:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in properies"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.iri"></v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddAxiomProperty = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addAxiomProperty">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddAxiomClass" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить домен для свойства</v-card-title>
    
                    <v-list>
                        <v-subheader>Классы:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in classes"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.iri"></v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddAxiomClass = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addAxiomProperty">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddRange" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить диапазон для свойства</v-card-title>
    
                    <v-list>
                        <v-subheader>Типы данных:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in xsdTypes"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="red darken-3">mdi-alpha-r-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.name"></v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddRange = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addAxiomProperty">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'DataPropertyes',
  data() {
      return{
          classes: [],
          properies: [],
          xsdTypes: [],
          activeProperty: {
              iri: null,
              name: null,
              domains: [],
              ranges: [],
              equivalents: [],
              disjointProps: [],
              superProps: [],
              subProps: [],
              isFunctional: false,
          },
          dialogAddProperty: false,
          dialogEditIRIProperty: false,
          dialogAddAxiomProperty: false,
          dialogAddAxiomClass: false,
          dialogAddRange: false,
          newProperty: null,
          newIRIProperty: null,
          selectedItem: null,
          typeAxiom: null
      }
  },

  created(){
      this.getAllClasses();
      this.getAllProperies();
      this.getXSDTypes();
  },

  methods: {

    getAllClasses(){
        this.classes =[];
        axios
        .get("http://localhost:8090/class/getAllClasses")
        .then(response => response.data)
        .then(data => Object.keys(data).map(clas => { 
            let item = {
                iri: clas,
                name: data[clas]
            };
            this.classes.push(item)
        }))
    },

    getAllProperies(){
        this.properies =[];
        axios
        .get("http://localhost:8090/dataProperty/getAllProperties")
        .then(response => response.data)
        .then(data => Object.keys(data).map(prop => { 
            let item = {
                iri: prop,
                name: data[prop]
            };
            this.properies.push(item)
        }))
    },

    getXSDTypes(){
        this.properies =[];
        axios
        .get("http://localhost:8090/dataProperty/getXSDTypes")
        .then(response => response.data)
        .then(data => Object.keys(data).map(prop => { 
            let item = {
                iri: prop,
                name: data[prop]
            };
            this.xsdTypes.push(item)
        }))
    },

    selectProperty(i){
        let file = new Blob([this.properies[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/dataProperty/getPropertyDescription" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => (
            this.activeProperty.iri = this.properies[i].iri,
            this.activeProperty.name = this.properies[i].name,
            this.activeProperty.domains = data.domains,
            this.activeProperty.ranges = data.ranges,
            this.activeProperty.equivalents = data.equivalents,
            this.activeProperty.disjointProps = data.disjointProps,
            this.activeProperty.superProps = data.superProps,
            this.activeProperty.subProps = data.subProps,
            this.activeProperty.isFunctional = Boolean(Number(data.isFunctional))
        ))
    },

    addNewProperty(){
        if(this.newProperty != null){
            this.dialogAddProperty = false;
            let file = new Blob([this.newClass]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/dataProperty/addNewProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                let item = {
                    iri: data.propertyIRI,
                    name: data.propertyName
                };
                this.properies.push(item);
                this.newProperty = null
            })
        }
    },

    editIRIProperty(){
        if(this.newIRIProperty != null && this.activeProperty.iri != null){
            this.dialogEditIRIProperty = false;
            let file = new Blob([this.activeProperty.iri,";",this.newIRIProperty]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/dataProperty/editIRIProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                this.newIRIProperty = null;
                let item = {
                    iri: data.propertyIRI,
                    name: data.propertyName
                };
                this.activeProperty.iri= item.iri;
                this.activeProperty.name = item.name;
                this.getAllProperies();
            })
        }
    },

    deleteProperty(i){
        let file = new Blob([this.properies[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/dataProperty/deleteProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                this.activeProperty.iri = null,
                this.activeProperty.name = null,
                this.activeProperty.domains = [],
                this.activeProperty.ranges = [],
                this.activeProperty.equivalents = [],
                this.activeProperty.disjointProps = [],
                this.activeProperty.superProps = [],
                this.activeProperty.subProps = [],
                this.activeProperty.isFunctional = false,
                this.properies.splice(i,1)
            }
        })
    },

    addAxiomProperty(){
        if(this.selectedItem != null){
            this.dialogAddRange = false;
            this.dialogAddAxiomClass = false;
            this.dialogAddAxiomProperty = false;
            let file = new Blob([this.activeProperty.iri,";",this.selectedItem.iri,"!",this.typeAxiom]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/dataProperty/addAxiomProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                if(data == true){
                    switch(this.typeAxiom){
                        case "domain": this.activeProperty.domains.push(this.selectedItem.iri); break;
                        case "range": this.activeProperty.ranges.push(this.selectedItem.name); break;
                        case "equivalent": this.activeProperty.equivalents.push(this.selectedItem.iri); break;
                        case "disjoint": this.activeProperty.disjointProps.push(this.selectedItem.iri); break;
                        case "subProp": this.activeProperty.subProps.push(this.selectedItem.iri); break;
                        case "superProp": this.activeProperty.superProps.push(this.selectedItem.iri); break;
                    }
                }
                this.typeAxiom = null
            })
        }
    },

    deleteAxiomProperty(i, axiom){
        let obj;
        switch(axiom){
            case "domain": obj = this.activeProperty.domains[i]; break;
            case "range": obj = this.activeProperty.ranges[i]; break;
            case "inverse": obj = this.activeProperty.inverseProps[i]; break;
            case "equivalent": obj = this.activeProperty.equivalents[i]; break;
            case "disjoint": obj = this.activeProperty.disjointProps[i]; break;
            case "subProp": obj = this.activeProperty.subProps[i]; break;
            case "superProp": obj = this.activeProperty.superProps[i]; break;
        }
        let file = new Blob([this.activeProperty.iri,";", obj, "!", axiom]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/dataProperty/deleteAxiomProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                switch(axiom){
                    case "domain": this.activeProperty.domains.splice(i,1); break;
                    case "range": this.activeProperty.ranges.splice(i,1); break;
                    case "inverse": this.activeProperty.inverseProps.splice(i,1); break;
                    case "equivalent": this.activeProperty.equivalents.splice(i,1); break;
                    case "disjoint": this.activeProperty.disjointProps.splice(i,1); break;
                    case "subProp": this.activeProperty.subProps.splice(i,1); break;
                    case "superProp": this.activeProperty.superProps.splice(i,1); break;
                }
            }
        })
    },

    changeCharacter(){
        let file = new Blob([this.activeProperty.iri,";", this.activeProperty.isFunctional]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/dataProperty/changeCharacter" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
    }
  }
}
</script>