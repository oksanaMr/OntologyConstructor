<template>
  <div class="individuals">
    <v-container>
        <h2 class="display-1 ml-5">Объекты</h2>
        <v-row>

            <v-col cols=4>
                <v-card>
                    <v-list style="maxHeight: 600px; overflow-y: auto;">
                        <v-subheader>СПИСОК ОБЪЕКТОВ
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddIndividual = true">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>
                            <v-list-item
                                v-for="(item,i) in individuals"
                                :key="item.name"
                                v-on:click="selectIndividual(i)"
                            >
                                <v-list-item-icon>
                                    <v-icon color="purple" large>mdi-alpha-i-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.name"></v-list-item-title>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab v-on:click="deleteIndividual(i)">
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
                    <v-card-title>Описание {{activeIndividual.name}}</v-card-title>

                    <v-row no-gutters class="mt-3">
                        <v-col cols=2>
                            <v-subheader>Individual IRI</v-subheader>
                        </v-col>
                        <v-col cols=7>
                            <v-text-field readonly v-model="activeIndividual.iri"></v-text-field> 
                        </v-col>
                        <v-col cols=2>
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;green&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogEditIRIIndividual=true; newIRIIndividual=activeIndividual.iri">
                                    <v-icon>mdi-pencil</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-col>
                    </v-row>

                    <v-divider></v-divider>
                    
                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Типы:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;type&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeIndividual.types" :key="item">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomIndividual(i,&quot;type&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Эквивалентные объекты:
                           <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomIndividual = true; typeAxiom=&quot;equivalent&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeIndividual.sameIndividuals" :key="item">
                            <v-list-item-icon>
                                <v-icon color="purple">mdi-alpha-i-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomIndividual(i,&quot;equivalent&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Различные объекты:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomIndividual=true; typeAxiom=&quot;diffInd&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeIndividual.diffIndividuals" :key="item">
                            <v-list-item-icon>
                                <v-icon color="purple">mdi-alpha-i-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomIndividual(i,&quot;diffInd&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-divider></v-divider>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Объектные свойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddObjectProp = true; typeAxiom=&quot;objectProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                            <v-list-item v-for="(item,i) in activeIndividual.objectPropAxiom" :key="item">
                                <v-list-item-icon>
                                    <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-tooltip bottom>
                                        <template v-slot:activator="{ on }">
                                            <v-list-item-title v-text="item" v-on="on"></v-list-item-title>
                                        </template>
                                        <span v-text="item"></span>
                                    </v-tooltip>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deletePropIndividual(i,&quot;objectProp&quot;)">
                                            <v-icon>mdi-delete</v-icon>
                                        </v-btn>
                                    </v-hover>
                                </v-list-item-action>
                            </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Свойства данных:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddDataProp=true; typeAxiom=&quot;dataProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeIndividual.dataPropAxiom" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on }">
                                        <v-list-item-title v-text="item" v-on="on"></v-list-item-title>
                                    </template>
                                    <span v-text="item"></span>
                                </v-tooltip>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deletePropIndividual(i,&quot;dataProp&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Отрицательные объектные свойства:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddObjectProp=true; typeAxiom=&quot;negativeObjectProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                            <v-list-item v-for="(item,i) in activeIndividual.negativeObjectPropAxiom" :key="item">
                                <v-list-item-icon>
                                    <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-tooltip bottom>
                                        <template v-slot:activator="{ on }">
                                            <v-list-item-title v-text="item" v-on="on"></v-list-item-title>
                                        </template>
                                        <span v-text="item"></span>
                                    </v-tooltip>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deletePropIndividual(i,&quot;negativeObjectProp&quot;)">
                                            <v-icon>mdi-delete</v-icon>
                                        </v-btn>
                                    </v-hover>
                                </v-list-item-action>
                            </v-list-item>
                    </v-list>

                     <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Отрицательные свойства данных:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddDataProp=true; typeAxiom=&quot;negativeDataProp&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeIndividual.negativeDataPropAxiom" :key="item">
                            <v-list-item-icon>
                                <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-tooltip bottom>
                                    <template v-slot:activator="{ on }">
                                        <v-list-item-title v-text="item" v-on="on"></v-list-item-title>
                                    </template>
                                    <span v-text="item"></span>
                                </v-tooltip>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deletePropIndividual(i,&quot;negativeDataProp&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                </v-card>
            </v-col>

        </v-row>

        <v-dialog v-model="dialogAddIndividual" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить новый объект</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=2>
                                <v-subheader>Object IRI</v-subheader>
                            </v-col>
                            <v-col cols=9>
                                <v-text-field class="form-control" v-model="newIndividual" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn  color="primary" text @click="dialogAddIndividual = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addNewIndividual">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogEditIRIIndividual" max-width="700px">
            <v-card>
                <v-card-title class="headline">Изменить IRI объекта</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=3>
                                <v-subheader>New Object IRI</v-subheader>
                            </v-col>
                            <v-col cols=8>
                                <v-text-field class="form-control" v-model="newIRIIndividual" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogEditIRIIndividual = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="editIRIIndividual">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddAxiomIndividual" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить аксиому для объекта</v-card-title>
    
                    <v-list>
                        <v-subheader>Индивидуальные объекты:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in individuals"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="purple">mdi-alpha-i-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.iri"></v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn  color="primary" text @click="dialogAddAxiomIndividual = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addAxiomIndividual">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddAxiomClass" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить аксиому для объекта</v-card-title>
    
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
                        <v-btn type="submit" color="primary" text @click="addAxiomIndividual">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddObjectProp" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить свойство для объекта</v-card-title>
    
                    <v-container>
                        <v-row>
                            <v-col cols="6">
                                <v-list>
                                    <v-subheader>Объектное свойство:</v-subheader>
                                    <v-list-item-group>
                                        <v-list-item
                                            v-for="(item) in objectProps"
                                            :key="item.name"
                                            @click="selectedItem = item"
                                        >
                                            <v-list-item-icon>
                                                <v-icon color="blue darken-1">mdi-alpha-p-circle</v-icon>
                                            </v-list-item-icon>
                                            
                                            <v-list-item-content>
                                                <v-tooltip bottom >
                                                    <template v-slot:activator="{ on }">
                                                        <v-list-item-title v-text="item.name" v-on="on"></v-list-item-title>
                                                    </template>
                                                <span v-text="item.iri"></span>
                                                </v-tooltip>
                                            </v-list-item-content> 
                                        </v-list-item>
                                    </v-list-item-group>
                                </v-list>
                            </v-col>
                            <v-col cols="6">
                                <v-list>
                                    <v-subheader>Значение свойства:</v-subheader>
                                    <v-list-item-group>
                                        <v-list-item
                                            v-for="(item) in individuals"
                                            :key="item.name"
                                            @click="valueProp = item.iri"
                                        >
                                            <v-list-item-icon>
                                                <v-icon color="purple">mdi-alpha-i-circle</v-icon>
                                            </v-list-item-icon>
                                            <v-list-item-content>
                                                <v-tooltip bottom >
                                                    <template v-slot:activator="{ on }">
                                                        <v-list-item-title v-text="item.name" v-on="on"></v-list-item-title>
                                                    </template>
                                                <span v-text="item.iri"></span>
                                                </v-tooltip>
                                            </v-list-item-content>
                                        </v-list-item>
                                    </v-list-item-group>
                                </v-list>
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddObjectProp = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addPropIndividual">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddDataProp" max-width="600px">
            <v-card>
                <v-card-title class="headline">Добавить свойство для объекта</v-card-title>
    
                    <v-list>
                        <v-subheader>Свойство данных:</v-subheader>
                        <v-list-item-group>
                            <v-list-item
                                v-for="(item) in dataProps"
                                :key="item.name"
                                @click="selectedItem = item"
                            >
                                <v-list-item-icon>
                                    <v-icon color="green darken-1">mdi-alpha-p-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                   <v-tooltip bottom >
                                        <template v-slot:activator="{ on }">
                                            <v-list-item-title v-text="item.name" v-on="on"></v-list-item-title>
                                        </template>
                                    <span v-text="item.iri"></span>
                                    </v-tooltip>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>

                    <v-subheader>Значение свойства:</v-subheader>
                    <v-text-field placeholder="Введите значение свойства" class="form-control ml-5 mr-5" outlined v-model="valueProp" required></v-text-field>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="dialogAddDataProp = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addPropIndividual">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'Individuals',
  data() {
      return{
          classes: [],
          individuals: [],
          objectProps: [],
          dataProps: [],
          activeIndividual: {
              iri: null,
              name: null,
              types: [],
              sameIndividuals: [],
              diffIndividuals: [],
              objectPropAxiom: [],
              dataPropAxiom: [],
              negativeObjectPropAxiom: [],
              negativeDataPropAxiom: []
          },
          dialogAddIndividual: false,
          dialogEditIRIIndividual: false,
          dialogAddAxiomIndividual: false,
          dialogAddAxiomClass: false,
          dialogAddObjectProp: false,
          dialogAddDataProp: false,
          newIndividual: null,
          newIRIIndividual: null,
          selectedItem: null,
          valueProp: null,
          typeAxiom: null
      }
  },

  created(){
      this.getAllClasses();
      this.getAllIndividuals();
      this.getAllObjectProperies();
      this.getAllDataProperies();
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

    getAllIndividuals(){
        this.individuals =[];
        axios
        .get("http://localhost:8090/individual/getAllIndividuals")
        .then(response => response.data)
        .then(data => Object.keys(data).map(ind => { 
            let item = {
                iri: ind,
                name: data[ind]
            };
            this.individuals.push(item)
        }))
    },

    getAllObjectProperies(){
        this.objectProps =[];
        axios
        .get("http://localhost:8090/objectProperty/getAllProperties")
        .then(response => response.data)
        .then(data => Object.keys(data).map(prop => { 
            let item = {
                iri: prop,
                name: data[prop]
            };
            this.objectProps.push(item)
        }))
    },

    getAllDataProperies(){
        this.dataProps =[];
        axios
        .get("http://localhost:8090/dataProperty/getAllProperties")
        .then(response => response.data)
        .then(data => Object.keys(data).map(prop => { 
            let item = {
                iri: prop,
                name: data[prop]
            };
            this.dataProps.push(item)
        }))
    },

    selectIndividual(i){
        let file = new Blob([this.individuals[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/individual/getIndividualDescription" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => (
            this.activeIndividual.iri = this.individuals[i].iri,
            this.activeIndividual.name = this.individuals[i].name,
            this.activeIndividual.types = data.types,
            this.activeIndividual.sameIndividuals = data.sameIndividuals,
            this.activeIndividual.diffIndividuals = data.diffIndividuals,
            this.activeIndividual.objectPropAxiom = data.objectPropAxiom,
            this.activeIndividual.dataPropAxiom = data.dataPropAxiom,
            this.activeIndividual.negativeObjectPropAxiom = data.negativeObjectPropAxiom,
            this.activeIndividual.negativeDataPropAxiom = data.negativeDataPropAxiom
        ))
    },

    addNewIndividual(){
        if(this.newIndividual != null){
            this.dialogAddIndividual = false;
            let file = new Blob([this.newIndividual]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/individual/addNewIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                let item = {
                    iri: data.individualIRI,
                    name: data.individualName
                };
                this.individuals.push(item);
                this.newIndividual = null
            })
        }
    },

    editIRIIndividual(){
        if(this.newIRIIndividual != null && this.activeIndividual.iri != null){
            this.dialogEditIRIIndividual = false;
            let file = new Blob([this.activeIndividual.iri,";",this.newIRIIndividual]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/individual/editIRIIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                this.newIRIIndividual = null;
                let item = {
                    iri: data.individualIRI,
                    name: data.individualName
                };
                this.activeIndividual.iri= item.iri;
                this.activeIndividual.name = item.name;
                this.getAllIndividuals();
            })
        }
    },

    deleteIndividual(i){
        let file = new Blob([this.individuals[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/individual/deleteIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                this.activeIndividual.iri = null,
                this.activeIndividual.name = null,
                this.activeIndividual.types = [],
                this.activeIndividual.sameIndividuals = [],
                this.activeIndividual.diffIndividuals = [],
                this.activeIndividual.objectPropAxiom = [],
                this.activeIndividual.dataPropAxiom = [],
                this.activeIndividual.negativeObjectPropAxiom = [],
                this.activeIndividual.negativeDataPropAxiom = [],
                this.individuals.splice(i,1)
            }
        })
    },

    addAxiomIndividual(){
        if(this.selectedItem != null){
            this.dialogAddAxiomIndividual = false;
            this.dialogAddAxiomClass = false;
            let file = new Blob([this.activeIndividual.iri,";",this.selectedItem.iri,"!",this.typeAxiom]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/individual/addAxiomIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                if(data == true){
                    switch(this.typeAxiom){
                        case "type": this.activeIndividual.types.push(this.selectedItem.iri);break;
                        case "equivalent": this.activeIndividual.sameIndividuals.push(this.selectedItem.iri);break;
                        case "diffInd": this.activeIndividual.diffIndividuals.push(this.selectedItem.iri);break;
                    }
                }
                this.typeAxiom = null,
                this.valueProp = null
            })
        }
    },

    deleteAxiomIndividual(i, axiom){
        let obj;
        switch(axiom){
            case "type": obj = this.activeIndividual.types[i]; break;
            case "equivalent": obj = this.activeIndividual.sameIndividuals[i]; break;
            case "diffInd": obj = this.activeIndividual.diffIndividuals[i]; break;
        }
        let file = new Blob([this.activeIndividual.iri,";", obj, "!", axiom]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/individual/deleteAxiomIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                switch(axiom){
                    case "type": this.activeIndividual.types.splice(i,1); break;
                    case "equivalent": this.activeIndividual.sameIndividuals.splice(i,1); break;
                    case "diffInd": this.activeIndividual.diffIndividuals.splice(i,1); break;
                }
            }
        })
    },

    addPropIndividual(){
        if(this.selectedItem != null && this.valueProp != null){
            this.dialogAddObjectProp = false;
            this.dialogAddDataProp = false;
            let file = new Blob([this.activeIndividual.iri,";",this.selectedItem.iri,"*", this.valueProp,"!",this.typeAxiom]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/individual/addPropIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                if(data == true){
                    switch(this.typeAxiom){
                        case "objectProp": this.activeIndividual.objectPropAxiom.push(this.selectedItem.iri + " " + this.valueProp);break;
                        case "dataProp": this.activeIndividual.dataPropAxiom.push(this.selectedItem.iri + " " + this.valueProp);break;
                        case "negativeObjectProp": this.activeIndividual.negativeObjectPropAxiom.push(this.selectedItem.iri + " " + this.valueProp);break;
                        case "negativeDataProp": this.activeIndividual.negativeDataPropAxiom.push(this.selectedItem.iri + " " + this.valueProp);break;
                    }
                }
                this.typeAxiom = null,
                this.valueProp = null
            })
        }
    },

    deletePropIndividual(i, axiom){
        let obj;
        switch(axiom){
            case "objectProp": obj = this.activeIndividual.objectPropAxiom[i]; break;
            case "dataProp": obj = this.activeIndividual.dataPropAxiom[i]; break;
            case "negativeObjectProp": obj = this.activeIndividual.negativeObjectPropAxiom[i]; break;
            case "negativeDataProp": obj = this.activeIndividual.negativeDataPropAxiom[i]; break;
        }
        let file = new Blob([this.activeIndividual.iri,";", obj, "!", axiom]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/individual/deletePropIndividual" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                switch(axiom){
                    case "objectProp": this.activeIndividual.objectPropAxiom.splice(i,1); break;
                    case "dataProp": this.activeIndividual.dataPropAxiom.splice(i,1); break;
                    case "negativeObjectProp": this.activeIndividual.negativeObjectPropAxiom.splice(i,1); break;
                    case "negativeDataProp": this.activeIndividual.negativeDataPropAxiom.splice(i,1); break;
                }
            }
        })
    },
  }
}
</script>