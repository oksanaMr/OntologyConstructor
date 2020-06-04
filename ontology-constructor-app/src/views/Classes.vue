<template>
  <div class="classes">
    <v-container>
        <h2 class="display-1 ml-5">Классы</h2>

        <v-row>

            <v-col cols=4>
                <v-card>
                    <v-list style="maxHeight: 600px; overflow-y: auto;">
                        <v-subheader>СПИСОК КЛАССОВ
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddClass = true">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>
                            <v-list-item
                                v-for="(item,i) in classes"
                                :key="item.name"
                                v-on:click="selectClass(i)"
                            >
                                <v-list-item-icon>
                                    <v-icon color="amber" large>mdi-alpha-c-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item.name"></v-list-item-title>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab v-on:click="deleteClass(i)">
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
                    <v-card-title>Описание {{activeClass.name}}</v-card-title>

                    <v-row no-gutters class="mt-3">
                        <v-col cols=2>
                            <v-subheader>Class IRI</v-subheader>
                        </v-col>
                        <v-col cols=7>
                            <v-text-field readonly v-model="activeClass.iri"></v-text-field> 
                        </v-col>
                        <v-col cols=2>
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;green&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogEditIRIClass = true; newIRIClass=activeClass.iri">
                                    <v-icon>mdi-pencil</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-col>
                    </v-row>

                    <v-divider></v-divider>
                    
                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Индивидуальные объекты:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddIndividual = true; typeAxiom=&quot;individual&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item,i) in activeClass.individuals" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="purple">mdi-alpha-i-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomClass(i,&quot;individual&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Суперклассы:
                           <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;superclass&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeClass.superClasses" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomClass(i,&quot;superclass&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list style="maxHeight: 300px; overflow-y: auto;">
                        <v-subheader>Подклассы:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;subclass&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeClass.subClasses" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomClass(i,&quot;subclass&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                    <v-list>
                        <v-subheader>Эквивалентные классы:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;equivalent&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                            <v-list-item v-for="(item, i) in activeClass.equivalents" :key="item.name">
                                <v-list-item-icon>
                                    <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-title v-text="item"></v-list-item-title>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-hover v-slot:default="{ hover }">
                                        <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomClass(i,&quot;equivalent&quot;)">
                                            <v-icon>mdi-delete</v-icon>
                                        </v-btn>
                                    </v-hover>
                                </v-list-item-action>
                            </v-list-item>
                    </v-list>

                    <v-list>
                        <v-subheader>Непересекающиеся классы:
                            <v-hover v-slot:default="{ hover }">
                                <v-btn :color="hover ? &quot;pink&quot; : &quot;grey lighten-1&quot;" dark x-small fab class="ml-3" @click.stop="dialogAddAxiomClass = true; typeAxiom=&quot;disjoint&quot;">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-hover>
                        </v-subheader>

                        <v-list-item v-for="(item, i) in activeClass.disjointClasses" :key="item.name">
                            <v-list-item-icon>
                                <v-icon color="amber">mdi-alpha-c-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-action>
                                <v-hover v-slot:default="{ hover }">
                                    <v-btn :color="hover ? &quot;red&quot; : &quot;grey lighten-1&quot;" dark x-small fab @click="deleteAxiomClass(i,&quot;disjoint&quot;)">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-hover>
                            </v-list-item-action>
                        </v-list-item>
                    </v-list>

                </v-card>
            </v-col>

        </v-row>

        <v-dialog v-model="dialogAddClass" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить новый класс</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=2>
                                <v-subheader>Class IRI</v-subheader>
                            </v-col>
                            <v-col cols=9>
                                <v-text-field class="form-control" v-model="newClass" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn  color="primary" text @click="dialogAddClass = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addNewClass">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogEditIRIClass" max-width="700px">
            <v-card>
                <v-card-title class="headline">Изменить IRI класса</v-card-title>
    
                <form>
                    <v-container>
                        <v-row no-gutters>
                            <v-col cols=3>
                                <v-subheader>New Class IRI</v-subheader>
                            </v-col>
                            <v-col cols=8>
                                <v-text-field class="form-control" v-model="newIRIClass" required></v-text-field> 
                            </v-col>
                        </v-row>
                    </v-container>
        
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn  color="primary" text @click="dialogEditIRIClass = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="editIRIClass">Сохранить</v-btn>
                    </v-card-actions>
                </form>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddIndividual" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить объект для класса</v-card-title>
    
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
                        <v-btn  color="primary" text @click="dialogAddIndividual = false">Отмена</v-btn>
                        <v-btn type="submit" color="primary" text @click="addAxiomClass">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="dialogAddAxiomClass" max-width="700px">
            <v-card>
                <v-card-title class="headline">Добавить аксиому для класса</v-card-title>
    
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
                        <v-btn type="submit" color="primary" text @click="addAxiomClass">Сохранить</v-btn>
                    </v-card-actions>
            </v-card>
        </v-dialog>

    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'Classes',
  data() {
      return{
          classes: [],
          individuals: [],
          activeClass: {
              iri: null,
              name: null,
              individuals: [],
              superClasses: [],
              subClasses: [],
              equivalents: [],
              disjointClasses: []
          },
          dialogAddClass: false,
          dialogEditIRIClass: false,
          dialogAddIndividual: false,
          dialogAddAxiomClass: false,
          newClass: null,
          newIRIClass: null,
          selectedItem: null,
          typeAxiom: null
      }
  },

  created(){
      this.getAllClasses();
      this.getAllIndividuals();
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

    selectClass(i){
        let file = new Blob([this.classes[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/class/getClassDescription" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => (
            this.activeClass.iri = this.classes[i].iri,
            this.activeClass.name = this.classes[i].name,
            this.activeClass.individuals = data.individuals,
            this.activeClass.superClasses = data.superClasses,
            this.activeClass.subClasses = data.subClasses,
            this.activeClass.equivalents = data.equivalents,
            this.activeClass.disjointClasses = data.disjointClasses
        ))
    },

    addNewClass(){
        if(this.newClass != null){
            this.dialogAddClass = false;
            let file = new Blob([this.newClass]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/class/addNewClass" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                let item = {
                    iri: data.classIRI,
                    name: data.className
                };
                this.classes.push(item);
                this.newClass = null
            })
        }
    },

    editIRIClass(){
        if(this.newIRIClass != null && this.activeClass.iri != null){
            this.dialogEditIRIClass = false;
            let file = new Blob([this.activeClass.iri,";",this.newIRIClass]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/class/editIRIClass" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                this.newIRIClass = null;
                let item = {
                    iri: data.classIRI,
                    name: data.className
                };
                this.activeClass.iri= item.iri;
                this.activeClass.name = item.name;
                this.getAllClasses();
            })
        }
    },

    deleteClass(i){
        let file = new Blob([this.classes[i].iri]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/class/deleteClass" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                this.activeClass.iri = null,
                this.activeClass.name = null,
                this.activeClass.individuals = [],
                this.activeClass.superClasses = [],
                this.activeClass.subClasses = [],
                this.activeClass.equivalents = [],
                this.activeClass.disjointClasses = [],
                this.classes.splice(i,1)
            }
        })
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

    addAxiomClass(){
        if(this.selectedItem != null){
            this.dialogAddAxiomClass = false;
            this.dialogAddIndividual = false;
            let file = new Blob([this.activeClass.iri,";",this.selectedItem.iri,"!",this.typeAxiom]);
            let formData = new FormData();
            formData.append('file', file);
            axios
            .post("http://localhost:8090/class/addAxiomClass" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
            .then(response => response.data)
            .then(data => {
                if(data == true){
                    switch(this.typeAxiom){
                        case "individual": this.activeClass.individuals.push(this.selectedItem.iri);break;
                        case "superclass": this.activeClass.superClasses.push(this.selectedItem.iri);break;
                        case "subclass": this.activeClass.subClasses.push(this.selectedItem.iri);break;
                        case "equivalent": this.activeClass.equivalents.push(this.selectedItem.iri);break;
                        case "disjoint": this.activeClass.disjointClasses.push(this.selectedItem.iri);break;
                    }
                }
                this.typeAxiom = null
            })
        }
    },

    deleteAxiomClass(i, axiom){
        let class2;
        switch(axiom){
            case "individual": class2 = this.activeClass.individuals[i]; break;
            case "superclass": class2 = this.activeClass.superClasses[i]; break;
            case "subclass": class2 = this.activeClass.subClasses[i]; break;
            case "equivalent": class2 = this.activeClass.equivalents[i]; break;
            case "disjoint": class2 = this.activeClass.disjointClasses[i]; break;
        }
        let file = new Blob([this.activeClass.iri,";", class2, "!", axiom]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/class/deleteAxiomClass" , formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            if(data == true){
                switch(axiom){
                    case "individual": this.activeClass.individuals.splice(i,1); break;
                    case "superclass": this.activeClass.superClasses.splice(i,1); break;
                    case "subclass": this.activeClass.subClasses.splice(i,1); break;
                    case "equivalent": this.activeClass.equivalents.splice(i,1); break;
                    case "disjoint": this.activeClass.disjointClasses.splice(i,1); break;
                }
            }
        })
    }
  }
}
</script>