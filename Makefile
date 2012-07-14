all: clean createpdf viewpdf

createpdf:
	pdflatex report.tex report.pdf

viewpdf:
	evince report.pdf &

clean:
	rm -rf *.log cv.pdf *.blg *.aux
