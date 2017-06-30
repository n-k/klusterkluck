import {Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {FlowsApi} from "../../client";

declare var go: any;

@Component({
  selector: 'app-flow',
  templateUrl: './flow.component.html',
  styles: []
})
export class FlowComponent implements OnInit {

  @ViewChild('diagram') diagramEl;
  @ViewChild('palette') paletteEl;
  @ViewChild('modal') modal;
  @ViewChild('errorModal') errorModal;

  id: string = '';
  diagram: any;
  node: any;
  error: string = '';
  message: string = '';

  constructor(private route: ActivatedRoute,
              private service: FlowsApi,) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
    });
  }

  ngAfterViewInit() {
    var $ = go.GraphObject.make;
    var diagram = $(go.Diagram,
      this.diagramEl.nativeElement,
      {
        initialContentAlignment: go.Spot.Center,
        allowDrop: true,  // must be true to accept drops from the Palette
        "animationManager.duration": 800, // slightly longer than default (600ms) animation
        "undoManager.isEnabled": true,  // enable undo & redo
        "maxSelectionCount": 1,
      });
    this.diagram = diagram;

    var lightText = 'whitesmoke';
    diagram.nodeTemplateMap.add("fn",  // the default category
      $(go.Node, "Spot", this.nodeStyle(),
        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
        $(go.Panel, "Auto",
          $(go.Shape, "Rectangle",
            {fill: "#00A9C9", stroke: null},
            new go.Binding("figure", "figure")),
          $(go.TextBlock,
            {
              font: "bold 11pt Helvetica, Arial, sans-serif",
              stroke: lightText,
              margin: 8,
              maxSize: new go.Size(160, NaN),
              wrap: go.TextBlock.WrapFit,
              editable: true
            },
            new go.Binding("text").makeTwoWay())
        ),
        // four named ports, one on each side:
        this.makePort("T", go.Spot.Top, true, true),
        this.makePort("L", go.Spot.Left, true, true),
        this.makePort("R", go.Spot.Right, true, true),
        this.makePort("B", go.Spot.Bottom, true, true)
      ));
    diagram.nodeTemplateMap.add("connector",  // the default category
      $(go.Node, "Spot", this.nodeStyle(),
        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
        $(go.Panel, "Auto",
          $(go.Shape, "Rectangle",
            {fill: "#00A9C9", stroke: null},
            new go.Binding("figure", "figure")),
          $(go.TextBlock,
            {
              font: "bold 11pt Helvetica, Arial, sans-serif",
              stroke: lightText,
              margin: 8,
              maxSize: new go.Size(160, NaN),
              wrap: go.TextBlock.WrapFit,
              editable: true
            },
            new go.Binding("text").makeTwoWay())
        ),
        // four named ports, one on each side:
        this.makePort("T", go.Spot.Top, true, false),
        this.makePort("L", go.Spot.Left, true, false),
        this.makePort("R", go.Spot.Right, true, false),
        this.makePort("B", go.Spot.Bottom, true, false)
      ));
    // replace the default Link template in the linkTemplateMap
    diagram.linkTemplate =
      $(go.Link,  // the whole link panel
        {
          routing: go.Link.AvoidsNodes,
          curve: go.Link.JumpOver,
          corner: 5, toShortLength: 4,
          relinkableFrom: true,
          relinkableTo: true,
          reshapable: true,
          resegmentable: true,
          // mouse-overs subtly highlight links:
          mouseEnter: function (e, link) {
            link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
          },
          mouseLeave: function (e, link) {
            link.findObject("HIGHLIGHT").stroke = "transparent";
          }
        },
        new go.Binding("points").makeTwoWay(),
        $(go.Shape,  // the highlight shape, normally transparent
          {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
        $(go.Shape,  // the link path shape
          {isPanelMain: true, stroke: "gray", strokeWidth: 2}),
        $(go.Shape,  // the arrowhead
          {toArrow: "standard", stroke: null, fill: "gray"}),
        $(go.Panel, "Auto",  // the link label, normally not visible
          {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
          new go.Binding("visible", "visible").makeTwoWay(),
          $(go.Shape, "RoundedRectangle",  // the label shape
            {fill: "#F8F8F8", stroke: null}),
          $(go.TextBlock, "Yes",  // the label
            {
              textAlign: "center",
              font: "10pt helvetica, arial, sans-serif",
              stroke: "#333333",
              editable: true
            },
            new go.Binding("text").makeTwoWay())
        )
      );
    // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
    diagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
    diagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

    // initialize the Palette that is on the left side of the page
    var palette = $(go.Palette, this.paletteEl.nativeElement,  // must name or refer to the DIV HTML element
      {
        "animationManager.duration": 800, // slightly longer than default (600ms) animation
        nodeTemplateMap: diagram.nodeTemplateMap,  // share the templates used by diagram
        model: new go.GraphLinksModel([  // specify the contents of the Palette
          {category: "connector", text: "Connector"},
          {category: "fn", text: "Function"},
        ])
      });

    this.service.getModel(this.id)
      .subscribe(
        m => {
          diagram.model = go.Model.fromJson(this.fromServerModel(m));
          diagram.addDiagramListener(
            "ChangedSelection",
            (event) => {
              const selection = event.diagram.selection.toArray() || [];
              if (selection.length) {
                const part = selection[0];
                if (part && part.data && part.data.category) {
                  this.node = part.data;
                } else {
                  this.node = null;
                }
              } else {
                this.node = null;
              }
            })
        },
        error => alert(error.toString())
      );
  }

  save() {
    const json = JSON.parse(this.diagram.model.toJson());
    this.message = 'saving...';
    this.modal.open();
    this.service.saveModel(this.id, this.toServerModel(json))
      .subscribe(
        x => {
          this.modal.close();
        },
        (error: Error) => {
          this.modal.close();
          this.errorModal.open();
          this.error = JSON.stringify(error);
        });
  }

  deploy() {
    this.message = 'deploying...';
    this.modal.open();
    this.service.deploy(this.id)
      .subscribe(
        x => {
          this.modal.close();
        },
        (error: Error) => {
          this.modal.close();
          this.errorModal.open();
          this.error = JSON.stringify(error);
        });
  }

  deleteNode() {
    var nodeToDelete = this.diagram.selection.iterator.first();
    this.diagram.remove(nodeToDelete);
    this.node = null;
  }

  private fromServerModel(model) {
    return {
      class: "go.GraphLinksModel",
      linkFromPortIdProperty: "fromPort",
      linkToPortIdProperty: "toPort",
      nodeDataArray: (model.nodes || [])
        .map(n => {
          return {
            key: n.id,
            ...n.data,
            ...n.uiProps
          };
        }),
      linkDataArray: (model.links || [])
        .map(l => {
          let {uiProps, ...rest} = l;
          return {
            ...uiProps,
            ...rest
          };
        })
    };
  }

  private toServerModel(model) {
    return {
      nodes: (model.nodeDataArray || [])
        .map(uin => {
          let {key, loc, ...rest} = uin;
          return {
            id: '' + key,
            data: rest,
            uiProps: {
              loc: loc
            }
          };
        }),
      links: (model.linkDataArray || [])
        .map(l => {
          let {fromPort, toPort, points, ...rest} = l;
          return {
            uiProps: {
              fromPort: fromPort,
              toPort: toPort,
              points: points
            },
            ...rest
          };
        })
    };
  }

  makePort(name, spot, output, input) {
    var $ = go.GraphObject.make;
    // the port is basically just a small circle that has a white stroke when it is made visible
    return $(go.Shape, "Circle",
      {
        fill: "transparent",
        stroke: null,  // this is changed to "white" in the showPorts function
        desiredSize: new go.Size(8, 8),
        alignment: spot, alignmentFocus: spot,  // align the port on the main Shape
        portId: name,  // declare this object to be a "port"
        fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
        fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
        cursor: "pointer"  // show a different cursor to indicate potential link point
      });
  }

  nodeStyle() {
    return [
      // The Node.location comes from the "loc" property of the node data,
      // converted by the Point.parse static method.
      // If the Node.location is changed, it updates the "loc" property of the node data,
      // converting back using the Point.stringify static method.
      new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
      {
        // the Node.location is at the center of each node
        locationSpot: go.Spot.Center,
        //isShadowed: true,
        //shadowColor: "#888",
        // handle mouse enter/leave events to show/hide the ports
        mouseEnter: (e, obj) => {
          this.showPorts(obj.part, true)
        },
        mouseLeave: (e, obj) => {
          this.showPorts(obj.part, false)
        }
      }
    ];
  }

  showPorts(node, show) {
    var diagram = node.diagram;
    if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
    node.ports.each(function (port) {
      port.stroke = (show ? "white" : null);
    });
  }

}
