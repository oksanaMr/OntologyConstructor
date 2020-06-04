<template>
  <div >
    <h2 class="display-1 ml-8 mt-5">Граф онтологии</h2>
    <cytoscape  ref="cy" id="cy" :config="config" :afterCreated="afterCreated" :preConfig="preConfig">
      <cy-element
        v-for="def in config.elements"
        :key="`${def.data.id}`"
        :definition="def"
      />
    </cytoscape>
  </div>
</template>

<script>

import axios from 'axios'
import dagre from 'cytoscape-dagre'
import $ from 'jquery';
import contextMenus from 'cytoscape-context-menus';
import 'cytoscape-context-menus/cytoscape-context-menus.css';

const config = {
    autounselectify: true,
    boxSelectionEnabled: false,
    wheelSensitivity: 0.5,
    style: [
      {
        selector: "node",
        css: {
          "width":50,
          "height" :50,
          "background-color": "#f92411",
          "label": "data(label)",
          "text-wrap": "wrap",
          "text-max-width": 100,
          "text-overflow-wrap":"anywhere",
          'text-valign': 'center',
        }
      },
      {
        selector: "node[group=\"individual\"]",
        css: {
          "background-color": "#87CEEB",
        }
      },
      {
        selector: "node[group=\"class\"]",
        css: {
          "background-color": "#FFD700",
        }
      },
      {
        selector: "edge",
        css: {
          "line-color": "#9bd8de",
          "edge-text-rotation": "autorotate",
          'target-arrow-shape': 'triangle',
          'target-arrow-color': '#9bd8de',
          'curve-style': 'bezier',
        }
      },
      {
        selector: "edge[group=\"has individual\"]",
        style: {
          "line-color": "#f0ec86",
          'target-arrow-color': '#f0ec86',
        }
      },
      {
        selector: "edge[group=\"subclass\"]",
        style: {
          "line-color": "#90e190",
          'target-arrow-color': '#90e190',
        }
      },
      {
        selector: "edge[group=\"equivalent\"]",
        style: {
          "line-color": "#9bd8de",
          'target-arrow-color': '#9bd8de',
        }
      },
      {
        selector: "edge[group=\"same individual\"]",
        style: {
          "line-color": "#eaa2a2",
          'target-arrow-color': '#eaa2a2',
        }
      }
    ],
    elements: [],
  };

export default {
  name: "Graph",
  data() {
    return {
        config,
      };
  },

  methods: {

    preConfig(cytoscape) {
      if (!cytoscape('core', 'contextMenus')) {
        cytoscape.use(contextMenus, $);
      }
      cytoscape.use(dagre);
      document.getElementById("cytoscape-div").style.minHeight="100%";
      
    },

    afterCreated(cy) {
      axios
        .get("http://localhost:8090/graph/getNodes")
        .then(response => response.data)
        .then(data => 
            Object.keys(data).map(node => {
              cy.add({data: {id: String(node), label: String(data[node][0]), group: String(data[node][1])}, group:"nodes"})
            }
        ));

      axios
      .get("http://localhost:8090/graph/getEdges")
      .then(response => response.data)
      .then(data => {
          Object.keys(data).map(edge => {
            cy.add({data: { id: String(edge), source: String(data[edge][0]), target: String(data[edge][1]), label: String(data[edge][2]), group: String(data[edge][2])}, group:"edges"})
          });

          cy.layout({
            name:"dagre",
            //idealEdgeLength: 200,
            nodeDimensionsIncludeLabels : true ,
            nodeSep : 15,
            edgeSep : 15,
            rankSep : 50,
            //tile: false,
            padding: 10,
            //nodeRepulsion: 6500,
            //klay: {
              //compactComponents: true,
            //}
          }).run();

          cy.contextMenus({menuItems: [										  
          {
            id: 'hide',
            content: 'скрыть элемент',
            selector: '*',
            onClickFunction: function (event) {
              let target = event.target || event.cyTarget;
              target.hide();
            },
            disabled: false
          },
          {
            id: 'select-all-nodes',
            content: 'показать все элементы',
            coreAsWell: true,
            onClickFunction: function () {
              cy.elements('node').show();
              cy.elements('edge').show();
            }
          },
          {
            id: 'show-label',
            content: 'показать название',
            selector: 'edge',
            onClickFunction: function (event) {
              let target = event.target || event.cyTarget;
              target.style("label", target.data().label);
            }
          },
          {
            id: 'hide-label',
            content: 'скрыть название',
            selector: 'edge',
            onClickFunction: function (event) {
              let target = event.target || event.cyTarget;
              target.style("label", "")
            }
          },
        ]
      })
        }
      );
    },
  }
};
</script>
<style scoped>
  #cy {
    width: 100%;
    height: 100%;
    position: absolute;
    top:0;
    left:0;
    text-align: left;
    display: block;
  }
</style>
