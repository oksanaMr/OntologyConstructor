<template>
  <div class="objectPropery">
    <v-container>
        <h2 class="display-1 ml-5">Объектные свойства</h2>

        <v-row>

            <v-col cols=4>
                <v-card>
                    <v-list style="maxHeight: 600px; overflow-y: auto;">
                        <v-subheader>СПИСОК ОБЪЕКТНЫХ СВОЙСТВ
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
                                    <v-icon color="blue darken-1" large>mdi-alpha-p-circle</v-icon>
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
                            <v-row>
                                <v-col cols="6">
                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isFunctional" color="primary" @change="changeCharacter(&quot;functional&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Функциональное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>

                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isInverseFunctional" color="primary" @change="changeCharacter(&quot;inverseFunctional&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Обратно функциональное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                            
                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isTransitive" color="primary" @change="changeCharacter(&quot;transitive&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Транзитивное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>

                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isSymmetric" color="primary" @change="changeCharacter(&quot;symmetric&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Симметричное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-col>
                
                                <v-col cols="6">
                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isAsymmetric" color="primary" @change="changeCharacter(&quot;asymmetric&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Асимметричное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>

                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isReflexive" color="primary" @change="changeCharacter(&quot;reflexive&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Рефлексивное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>

                                    <v-list-item>
                                        <v-list-item-action>
                                            <v-checkbox v-model="activeProperty.isIrreflexive" color="primary" @change="changeCharacter(&quot;irreflexive&quot;)"></v-checkbox>
                                        </v-list-item-action>
                                        <v-list-item-content>
                                            <v-list-item-title>Нерефлексивное</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-col>
                            </v-row>
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

                        <v-list-item v-for="(item, i) in activeProperty.domains" :key="item.name">
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
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass=true; typeAxiom=&quot;range&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.ranges" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
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

                        <v-list-item v-for="(item, i) in activeProperty.equivalents" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
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
                        <v-subheader>Обратные свойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomProperty=true; typeAxiom=&quot;inverse&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeProperty.inverseProps" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomProperty(i,&quot;inverse&quot;)">
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

                        <v-list-item v-for="(item, i) in activeProperty.disjointProps" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
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
                        <v-list-item v-for="(item, i) in activeProperty.superProps" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
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

                        <v-list-item v-for="(item, i) in activeProperty.subProps" :key="item.name" >
                            <v-list-item-icon>
                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
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
                <v-card-title class="headline">Добавить новый объектное свойство</v-card-title>
    
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
                        <v-subheader>Объектные свойства:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in properies"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
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
                <v-card-title class="headline">Добавить аксиому для свойства</v-card-title>
    
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

    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'ObjectProperties',
  data() {
      return{
          classes: [],
          properies: [],
          activeProperty: {
              iri: null,
              name: null,
              domains: [],
              ranges: [],
              inverseProps: [],
              equivalents: [],
              disjointProps: [],
              superProps: [],
              subProps: [],
              isFunctional: false,
              isInverseFunctional: false,
              isTransitive: false,
              isSymmetric: false,
              isAsymmetric: false,
              isReflexive: false,
              isIrreflexive: false
          },
          dialogAddProperty: false,
          dialogEditIRIProperty: false,
          dialogAddAxiomProperty: false,
          dialogAddAxiomClass: false,
          newProperty: null,
          newIRIProperty: null,
          selectedItem: null,
          typeAxiom: null
      }
  },

  created(){
      this.getAllClasses();
      this.getAllProperies();
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
        .get("http://localhost:8090/objectProperty/getAllProperties")
        .then(response => response.data)
        .then(data => Object.keys(data).map(prop => { 
            let item = {
                iri: prop,
                name: data[prop]
            };
            this.properies.push(item)
        }))
    },

    selectProperty(i){
        let file = new Blob([this.properies[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/objectProperty/getPropertyDescription" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => (
            this.activeProperty.iri = this.properies[i].iri,
            this.activeProperty.name = this.properies[i].name,
            this.activeProperty.domains = data.domains,
            this.activeProperty.ranges = data.ranges,
            this.activeProperty.inverseProps = data.inverseProps,
            this.activeProperty.equivalents = data.equivalents,
            this.activeProperty.disjointProps = data.disjointProps,
            this.activeProperty.superProps = data.superProps,
            this.activeProperty.subProps = data.subProps,
            this.activeProperty.isFunctional = Boolean(Number(data.isFunctional)),
            this.activeProperty.isInverseFunctional = Boolean(Number(data.isInverseFunctional)),
            this.activeProperty.isTransitive = Boolean(Number(data.isTransitive)),
            this.activeProperty.isSymmetric = Boolean(Number(data.isSymmetric)),
            this.activeProperty.isAsymmetric = Boolean(Number(data.isAsymmetric)),
            this.activeProperty.isReflexive = Boolean(Number(data.isReflexive)),
            this.activeProperty.isIrreflexive = Boolean(Number(data.isIrreflexive))
        ))
    },

    addNewProperty(){
        if(this.newProperty != null){
            this.dialogAddProperty = false;
            let file = new Blob([this.newProperty]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/objectProperty/addNewProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
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
            .post("http://localhost:8090/objectProperty/editIRIProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
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
        .post("http://localhost:8090/objectProperty/deleteProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                this.activeProperty.iri = null,
                this.activeProperty.name = null,
                this.activeProperty.domains = [],
                this.activeProperty.ranges = [],
                this.activeProperty.inverseProps = [],
                this.activeProperty.equivalents = [],
                this.activeProperty.disjointProps = [],
                this.activeProperty.superProps = [],
                this.activeProperty.subProps = [],
                this.activeProperty.isFunctional = false,
                this.activeProperty.isInverseFunctional = false,
                this.activeProperty.isTransitive = false,
                this.activeProperty.isSymmetric = false,
                this.activeProperty.isAsymmetric = false,
                this.activeProperty.isReflexive = false,
                this.activeProperty.isIrreflexive = false,
                this.properies.splice(i,1)
            }
        })
    },

    addAxiomProperty(){
        if(this.selectedItem != null){
            this.dialogAddAxiomClass = false;
            this.dialogAddAxiomProperty = false;
            let file = new Blob([this.activeProperty.iri,";",this.selectedItem.iri,"!",this.typeAxiom]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/objectProperty/addAxiomProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                if(data == true){
                    switch(this.typeAxiom){
                        case "domain": this.activeProperty.domains.push(this.selectedItem.iri); break;
                        case "range": this.activeProperty.ranges.push(this.selectedItem.iri); break;
                        case "inverse": this.activeProperty.inverseProps.push(this.selectedItem.iri); break;
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
        .post("http://localhost:8090/objectProperty/deleteAxiomProperty" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
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

    changeCharacter(axiom){
        let value;
        switch(axiom){
            case "functional": value = this.activeProperty.isFunctional; break;
            case "inverseFunctional": value = this.activeProperty.isInverseFunctional; break;
            case "transitive": value = this.activeProperty.isTransitive; break;
            case "symmetric": value = this.activeProperty.isSymmetric; break;
            case "asymmetric": value = this.activeProperty.isAsymmetric; break;
            case "reflexive": value = this.activeProperty.isReflexive; break;
            case "irreflexive": value = this.activeProperty.isIrreflexive; break;
        }
        let file = new Blob([this.activeProperty.iri,";", value, "!", axiom]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/objectProperty/changeCharacter" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
    }
  }
}
</script>