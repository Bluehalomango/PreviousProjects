(function() {

    var width, height, largeHeader, canvas, ctx, triangles, target, animateHeader = true;

    initHeader();
    addListeners();
    initAnimation();

    function initHeader() {
        width = window.innerWidth;
        height = window.innerHeight;
        target = {x: 0, y: height};

        largeHeader = document.getElementById('titleCard');
        largeHeader.style.height = height+'px';

        canvas = document.getElementById('canvas');
        canvas.width = width;
        canvas.height = height;
        ctx = canvas.getContext('2d');

        triangles = [];
        for(var x = 0; x < 480; x++) {
            addTriangle(x*10);
        }
    }

    function addTriangle(delay) {
        setTimeout(function() {
            var t = new Triangle();
            triangles.push(t);
            tweenTriangle(t);
        }, delay);
    }

    function initAnimation() {
        animate();
    }

    function tweenTriangle(tri) {
        var t = Math.random()*(2*Math.PI);
        var x = (200+Math.random()*100)*Math.cos(t) + width*0.5;
        var y = (200+Math.random()*100)*Math.sin(t) + height*0.5;
        var time = 2+3*Math.random();

        gsap.to(tri.pos, time, {x: x,
            y: y, ease:Circ.easeOut,
            onComplete: function() {
                tri.init();
                tweenTriangle(tri);
        }});
    }

    //Event handling for the user scroll
    function addListeners() {
        window.addEventListener('scroll', scrollCheck);
        window.addEventListener('resize', resize);

        //I added this code to hide the animation once the user clicks the landing page button
        document.getElementById("title").addEventListener("click", function(){
            canvas = document.getElementById('canvas').style.display = "none";
            animateHeader = false;
        } );
    }

    function scrollCheck() {
        if(document.body.scrollTop > height) animateHeader = false;
        else animateHeader = true;
    }

    function resize() {
        width = window.innerWidth;
        height = window.innerHeight;
        largeHeader.style.height = height+'px';
        canvas.width = width;
        canvas.height = height;
    }

    function animate() {
        if(animateHeader) {
            ctx.clearRect(0,0,width,height);
            for(var i in triangles) {
                triangles[i].draw();
            }
        }
        requestAnimationFrame(animate);
    }

    function Triangle() {
        var _this = this;

        (function() {
            _this.coords = [{},{},{}];
            _this.pos = {};
            init();
        })();

        function init() {
            _this.pos.x = width*0.5;
            _this.pos.y = height*0.5-20;
            _this.coords[0].x = -10+Math.random()*40;
            _this.coords[0].y = -10+Math.random()*40;
            _this.coords[1].x = -10+Math.random()*40;
            _this.coords[1].y = -10+Math.random()*40;
            _this.coords[2].x = -10+Math.random()*40;
            _this.coords[2].y = -10+Math.random()*40;
            _this.scale = 0.1+Math.random()*0.3;
            // Changed code for colour to generate a random blue colour
            // Originally, the function used a set array of colours but create a more dynamic function, I altered this to create a completely random colour.
            //I decided this didn't suit my aesthetic and changed it to what it is now, the random blue colour.
            _this.color = generateBlueColour();
            setTimeout(function() { _this.alpha = 0.8; }, 10);
        }

        //Function to generate completely random colour
        function generateColour() {
            return (Math.floor(Math.random()*255) + ',' + Math.floor(Math.random()*255) + ',' + Math.floor(Math.random()*255));
        }

        //Function to generate random blue shade
        function generateBlueColour() {
            value = Math.floor(Math.random()*255);
            if (value > 70) {
                //Blue colour made by setting red as 0, blue as 255 and randomly asssigning green for the shade
                return ('0,' + Math.floor(Math.random()*255) + ',255');
            } else {
                //Blue colour made by setting red and green as 0 and randomly asssigning blue from a value between 0 and 100, with a added adjustment of 120 (i.e. lowest blue value is 120)
                return ('0,0,' + (Math.floor(Math.random()*100) + 120));
            }
        }

        this.draw = function() {
            if(_this.alpha >= 0.005) _this.alpha -= 0.005;
            else _this.alpha = 0;
            ctx.beginPath();
            ctx.moveTo(_this.coords[0].x+_this.pos.x, _this.coords[0].y+_this.pos.y);
            ctx.lineTo(_this.coords[1].x+_this.pos.x, _this.coords[1].y+_this.pos.y);
            ctx.lineTo(_this.coords[2].x+_this.pos.x, _this.coords[2].y+_this.pos.y);
            ctx.closePath();
            ctx.fillStyle = 'rgba('+_this.color+','+ _this.alpha+')';
            ctx.fill();
        };

        this.init = init;
    }
    
})();